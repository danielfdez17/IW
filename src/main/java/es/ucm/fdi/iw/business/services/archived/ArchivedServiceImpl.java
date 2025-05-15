package es.ucm.fdi.iw.business.services.archived;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.ucm.fdi.iw.business.dto.ArchivedDTO;
import es.ucm.fdi.iw.business.model.Subasta;
import es.ucm.fdi.iw.business.repository.SubastaRepository;

@Service
public class ArchivedServiceImpl implements ArchivedServices {

    @Autowired
    private SubastaRepository subastaRepository;

    @Override
    public List<ArchivedDTO> getArchived() {
        // Obtener todas las subastas
        List<Subasta> subastas = subastaRepository.getAllSubastas();
    
        return subastas.stream()
            .map(subasta -> {
                String usuario = subasta.getPujas().isEmpty() ? "No hay ganador" :
                    subasta.getPujas().get(subasta.getPujas().size() - 1).getUser().getUsername();
    
                String fecha = subasta.getFechaFin().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    
                return new ArchivedDTO(
                    String.valueOf(subasta.getId()),
                    subasta.getNombre(),
                    subasta.getRutaImagen(),
                    subasta.getPrecioActual() + " €",
                    usuario,
                    fecha,
                    subasta.getValoracionGanador(),
                    subasta.getComentarioGanador(),
                    subasta.getEstado()
                );
            })
            .toList();
    }
    
}
