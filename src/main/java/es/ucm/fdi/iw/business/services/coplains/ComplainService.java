package es.ucm.fdi.iw.business.services.coplains;

import java.util.List;

import es.ucm.fdi.iw.business.dto.ComplainDTO;
import es.ucm.fdi.iw.business.model.Complain;

public interface ComplainService {
    Complain createComplain(ComplainDTO complainDTO);

    List<ComplainDTO> getAllComplains();

    ComplainDTO complainToDTO(Complain complain);
}
