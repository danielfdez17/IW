package es.ucm.fdi.iw.business.services.historical;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.ucm.fdi.iw.business.dto.HistoricalDTO;
import es.ucm.fdi.iw.business.model.Subasta;
import es.ucm.fdi.iw.business.repository.SubastaRepository;

@Service
public class HistoricalServiceImpl implements HistoricalServices {

    @Autowired
    private SubastaRepository subastaRepository;

    @Override
    public List<HistoricalDTO> getHistorical() {
        // Obtener todas las subastas
        List<Subasta> subastas = subastaRepository.getAllSubastas();
    
        return subastas.stream()
            .filter(subasta -> subasta.isEnabled())
            .map(subasta -> {
                String usuario = subasta.getPujas().isEmpty() ? "No hay pujas" :
                    subasta.getPujas().get(subasta.getPujas().size() - 1).getUser().getUsername();
    
                String fecha = subasta.getFechaFin().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    
                return new HistoricalDTO(
                    String.valueOf(subasta.getId()),
                    subasta.getNombre(),
                    subasta.getRutaImagen(),
                    subasta.getPrecioActual() + " €",
                    usuario,
                    fecha
                );
            })
            .collect(Collectors.toList());
    }
    
}
