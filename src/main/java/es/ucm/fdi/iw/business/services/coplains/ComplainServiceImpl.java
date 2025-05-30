package es.ucm.fdi.iw.business.services.coplains;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import es.ucm.fdi.iw.business.dto.ComplainDTO;
import es.ucm.fdi.iw.business.mapper.ComplainMapper;
import es.ucm.fdi.iw.business.model.Complain;
import es.ucm.fdi.iw.business.model.User;
import es.ucm.fdi.iw.business.repository.ComplainRepository;
import es.ucm.fdi.iw.business.services.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ComplainServiceImpl implements ComplainService {
    private final ComplainRepository complainRepository;
    private final UserService userService;

    @Override
    public Complain createComplain(ComplainDTO complainDTO) {
        User creador = userService.findUserById(complainDTO.getCreatorId());

        if (creador == null) {
            throw new EntityNotFoundException(
                    String.format("No existe el usuario con id %s", complainDTO.getCreatorId()));
        }

        Complain complain = Complain.builder()
                .creador(creador)
                .dateTime(LocalDateTime.now())
                .text(complainDTO.getText())
                .build();

        Complain c = complainRepository.save(complain);
        return c;
    }

    @Override
    public List<ComplainDTO> getAllComplains() {
        return complainRepository.findAll().stream().map(this::complainToDTO).toList();
    }

    @Override
    public ComplainDTO complainToDTO(Complain complain) {
        return ComplainMapper.INSTANCE.entityToDTO(complain);
    }

}
