package es.ucm.fdi.iw.business.services.subastas;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.mapping.Array;
import org.springframework.stereotype.Service;

import es.ucm.fdi.iw.business.dto.SubastaDTO;
import es.ucm.fdi.iw.business.enums.EstadoSubasta;
import es.ucm.fdi.iw.business.mapper.SubastaMapper;
import es.ucm.fdi.iw.business.model.Subasta;
import es.ucm.fdi.iw.business.model.User;
import es.ucm.fdi.iw.business.repository.SubastaRepository;
import es.ucm.fdi.iw.business.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@AllArgsConstructor
@Log4j2
public class SubastaServicesImpl implements SubastasServices {

    private final SubastaRepository subastaRepository;
    private final UserRepository userRepository;

    @Override
    public List<SubastaDTO> getSubastasByStatus(EstadoSubasta estado) { 
        return subastaRepository.findByEstado(estado).stream().map(SubastaMapper.INSTANCE::entityToDto).toList();
    }

    @Override
    public SubastaDTO updateSubasta(SubastaDTO subastaDTO) {
        Optional<Subasta> subastaOpt = this.subastaRepository.findById(subastaDTO.getId());
        if (subastaOpt.isEmpty() || EstadoSubasta.FINALIZADA.equals(subastaOpt.get().getEstado()))
            return null;  
        User user = userRepository.findById(subastaDTO.getIdUserGanador()).orElse(null);
        Subasta subasta = subastaOpt.get();
        subasta.setEstado(subastaDTO.getEstado());
        subasta.setEnabled(subastaDTO.isEnabled());
        subasta.setGanador(user);
        subasta.setRepartoSubasta(subastaDTO.getRepartoSubasta());
        subastaRepository.save(subasta);
        return SubastaMapper.INSTANCE.entityToDto(subasta);
    }

}
