package es.ucm.fdi.iw.business.services.puja;

import es.ucm.fdi.iw.business.dto.CreateProductDTO;
import es.ucm.fdi.iw.business.dto.PujaDTO;
import es.ucm.fdi.iw.business.mapper.PujaMapper;
import es.ucm.fdi.iw.business.mapper.SubastaMapper;
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

    @Override
    @Transactional
    public void updatePuja(PujaDTO pujaDTO) {
        // Verificar si la puja existe
        Optional<Puja> existingPuja = pujaRepository
                .findById(new PujaEmbed(pujaDTO.getUsuarioId(), pujaDTO.getSubastaId()));
        if (existingPuja.isPresent()) {
            Puja puja = existingPuja.get();
            puja.setFecha(LocalDateTime.now());
            puja.setDineroPujado(pujaDTO.getDineroPujado());
            puja.setPuntuacion(pujaDTO.getPuntuacion());
            puja.setComentario(pujaDTO.getComentario());
            puja.setFecha(pujaDTO.getFecha());
            User lastUser = puja.getSubasta().getPujas().getLast().getUser();
            double lastValue = puja.getSubasta().getPujas().getLast().getDineroPujado();
            lastUser.setAvailableMoney(lastUser.getAvailableMoney() + lastValue);

            // Guardar la puja actualizada
        } else {
            Subasta existingSubasta = subastaRepository.findById(pujaDTO.getSubastaId())
                    .orElseThrow(() -> new RuntimeException("No existe la subasta con id " + pujaDTO.getSubastaId()));
            User existingUser = userRepository.findById(pujaDTO.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("No existe el usuario con id " + pujaDTO.getUsuarioId()));
            if (existingSubasta.getPujas() != null && !existingSubasta.getPujas().isEmpty()) {
                User lastUser = existingSubasta.getPujas().getLast().getUser();
                double lastValue = existingSubasta.getPujas().getLast().getDineroPujado();
                lastUser.setAvailableMoney(lastUser.getAvailableMoney() + lastValue);
            }

            Puja puja = new Puja();
            puja.setId(new PujaEmbed(pujaDTO.getUsuarioId(), pujaDTO.getSubastaId()));
            puja.setFecha(LocalDateTime.now());
            puja.setDineroPujado(pujaDTO.getDineroPujado());
            puja.setPuntuacion(pujaDTO.getPuntuacion());
            puja.setComentario(pujaDTO.getComentario());
            puja.setFecha(pujaDTO.getFecha());
            puja.setSubasta(existingSubasta);
            puja.setUser(existingUser);
            pujaRepository.save(puja);
            // throw new RuntimeException("Puja no encontrada");
        }
    }


    @Override
    public List<PujaDTO> getPujasPorUsuario(long usuarioId) {
        // Obtener todas las pujas del usuario
        List<Puja> pujas = pujaRepository.findAllByUserId(usuarioId);

        // Convertir las pujas a DTO y añadir los detalles de la subasta
        return pujas.stream()
                    .map(puja -> {
                        PujaDTO pujaDTO = new PujaDTO();
                        pujaDTO.setUsuarioId(puja.getUser().getId());
                        pujaDTO.setSubastaId(puja.getSubasta().getId());
                        pujaDTO.setDineroPujado(puja.getDineroPujado());
                        pujaDTO.setPuntuacion(puja.getPuntuacion());
                        pujaDTO.setComentario(puja.getComentario());
                        pujaDTO.setFecha(puja.getFecha());

                        // Crear el CreateProductDTO con los detalles de la subasta
                        Subasta subasta = puja.getSubasta();
                        CreateProductDTO subastaDTO = new CreateProductDTO(
                            subasta.getFechaInicio().toString(),
                            subasta.getFechaFin().toString(),
                            subasta.getDescripcion(),
                            subasta.getPrecio(),
                            subasta.getNombre()
                        );

                        // Añadir el CreateProductDTO al PujaDTO
                        puja.setSubasta(subasta);

                        return pujaDTO;
                    })
                    .collect(Collectors.toList());
    }


}
