package es.ucm.fdi.iw.business.services.subastas;

import java.util.List;

import es.ucm.fdi.iw.business.dto.SubastaDTO;
import es.ucm.fdi.iw.business.enums.EstadoSubasta;

public interface SubastasServices {
    public List<SubastaDTO> getSubastasByStatus(EstadoSubasta estado);
    public SubastaDTO updateSubasta(SubastaDTO subastaDTO);
}
