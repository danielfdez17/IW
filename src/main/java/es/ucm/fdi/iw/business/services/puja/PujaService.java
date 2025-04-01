package es.ucm.fdi.iw.business.services.puja;

import es.ucm.fdi.iw.business.dto.PujaDTO;

import java.util.List;
import java.util.Map;

public interface PujaService {
    Map<Long, PujaDTO> getPujas();
    List<PujaDTO> getAllPujas();
    PujaDTO getPuja(long usuarioId, long subastaId);
    void savePuja(PujaDTO pujaDTO);
    void updatePuja(PujaDTO pujaDTO);
    public List<PujaDTO> getPujasPorUsuario(long usuarioId);
}

