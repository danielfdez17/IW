package es.ucm.fdi.iw.business.dto;

import java.time.LocalDateTime;

import es.ucm.fdi.iw.business.enums.EstadoSubasta;
import es.ucm.fdi.iw.business.enums.RepartoSubasta;
import lombok.Data;

@Data
public class SubastaDTO {
    private long id;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private boolean enabled;
    private String rutaImagen;
    private double precioInicial;
    private String descripcion;
    private String nombre;
    private double precioActual;
    private double precio;
    private String maximoPujador;
    private EstadoSubasta estado;
    private Long idUserCreator;
    private Long idUserGanador;
    private RepartoSubasta repartoSubasta;
    private String comentarioGanador;
    private Byte valoracionGanador;
}
