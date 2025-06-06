package es.ucm.fdi.iw.business.services.puja;

import es.ucm.fdi.iw.business.dto.PujaDTO;
import es.ucm.fdi.iw.business.mapper.PujaMapper;
import es.ucm.fdi.iw.business.model.Puja;
import es.ucm.fdi.iw.business.model.PujaEmbed;
import es.ucm.fdi.iw.business.model.Subasta;
import es.ucm.fdi.iw.business.model.User;
import es.ucm.fdi.iw.business.repository.PujaRepository;
import es.ucm.fdi.iw.business.repository.SubastaRepository;
import es.ucm.fdi.iw.business.repository.UserRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PujaServiceImpl implements PujaService {

    private final PujaRepository pujaRepository;
    private final SubastaRepository subastaRepository;
    private final UserRepository userRepository;

    @Autowired
    public PujaServiceImpl(PujaRepository pujaRepository, SubastaRepository subastaRepository,
            UserRepository userRepository) {
        this.pujaRepository = pujaRepository;
        this.subastaRepository = subastaRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Map<Long, PujaDTO> getPujas() {
        return pujaRepository.findAll().stream()
                .collect(Collectors.toMap(puja -> puja.getId().getSubastaId(), PujaMapper.INSTANCE::pujaToPujaDTO));
    }

    @Override
    public List<PujaDTO> getAllPujas() {
        return pujaRepository.findAll().stream()
                .map(PujaMapper.INSTANCE::pujaToPujaDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PujaDTO getPuja(long usuarioId, long subastaId) {
        Optional<Puja> puja = pujaRepository.findById(new PujaEmbed(usuarioId, subastaId));
        return puja.map(PujaMapper.INSTANCE::pujaToPujaDTO).orElse(null);
    }

    @Override
    public void savePuja(PujaDTO pujaDTO) {
        // Verificar si el usuario y la subasta existen
        Optional<User> userOpt = userRepository.findById(pujaDTO.getUsuarioId());
        Optional<Subasta> subastaOpt = subastaRepository.findById(pujaDTO.getSubastaId());

        if (userOpt.isPresent() && subastaOpt.isPresent()) {
            User user = userOpt.get();
            Subasta subasta = subastaOpt.get();

            // Crear la nueva puja
            Puja puja = new Puja();

            // Asignar la clave compuesta
            PujaEmbed pujaEmbed = new PujaEmbed(pujaDTO.getUsuarioId(), pujaDTO.getSubastaId());
            puja.setId(pujaEmbed); // Asignar la clave compuesta

            // Asignar los demás campos
            puja.setUser(user);
            puja.setSubasta(subasta);
            puja.setDineroPujado(pujaDTO.getDineroPujado());
            puja.setPuntuacion(pujaDTO.getPuntuacion());
            puja.setComentario(pujaDTO.getComentario());
            puja.setFecha(pujaDTO.getFecha());

            // Guardar la nueva puja
            pujaRepository.save(puja);
        } else {
            throw new RuntimeException("Usuario o Subasta no encontrados");
        }
    }

    @Transactional
    @Override
    public void updatePuja(PujaDTO pujaDTO){
        List<PujaDTO> listPujas = getPujasBySubastaId(pujaDTO.getSubastaId());
        Optional<PujaDTO> maxPujaOpt = listPujas.stream().max(Comparator.comparingDouble(PujaDTO::getDineroPujado));
        if (maxPujaOpt.isPresent() && maxPujaOpt.get().getDineroPujado() >= pujaDTO.getDineroPujado())
            return;
        
        Optional<Puja> existingPuja = pujaRepository.findById(new PujaEmbed(pujaDTO.getUsuarioId(), pujaDTO.getSubastaId()));
        if (existingPuja.isPresent()) {
            Puja puja = existingPuja.get();
            User user = puja.getUser();
            if (user.getAvailableMoney() + puja.getDineroPujado() < pujaDTO.getDineroPujado()) 
                throw new RuntimeException("El usuario no tiene suficiente dinero para pujar");   
            
            user.setAvailableMoney((user.getAvailableMoney() + puja.getDineroPujado())  - pujaDTO.getDineroPujado());
            puja.setFecha(LocalDateTime.now());
            puja.setDineroPujado(pujaDTO.getDineroPujado());
            puja.setPuntuacion(pujaDTO.getPuntuacion());
            puja.setComentario(pujaDTO.getComentario());
            puja.setFecha(pujaDTO.getFecha());
        } else {
            Subasta existingSubasta = subastaRepository.findById(pujaDTO.getSubastaId())
                    .orElseThrow(() -> new RuntimeException("No existe la subasta con id " + pujaDTO.getSubastaId()));
            User existingUser = userRepository.findById(pujaDTO.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("No existe el usuario con id " + pujaDTO.getUsuarioId()));
            Puja puja = new Puja();
            if (existingUser.getAvailableMoney() < pujaDTO.getDineroPujado()) 
                throw new RuntimeException("El usuario no tiene suficiente dinero para pujar");   
            puja.setId(new PujaEmbed(pujaDTO.getUsuarioId(), pujaDTO.getSubastaId()));
            puja.setFecha(LocalDateTime.now());
            puja.setDineroPujado(pujaDTO.getDineroPujado());
            puja.setPuntuacion(pujaDTO.getPuntuacion());
            puja.setComentario(pujaDTO.getComentario());
            puja.setFecha(pujaDTO.getFecha());
            puja.setSubasta(existingSubasta);
            puja.setUser(existingUser);
            existingUser.setAvailableMoney(existingUser.getAvailableMoney() - pujaDTO.getDineroPujado());
            pujaRepository.save(puja);
        }
    }
    
    @Override
    public List<PujaDTO> getPujasBySubastaId(long subastaId) {
        List<Puja> puja = pujaRepository.findBySubastaId(subastaId);
        if (puja.isEmpty()) 
            return List.of();
        
        return puja.stream()
                .map(PujaMapper.INSTANCE::pujaToPujaDTO)
                .toList();
    }
}