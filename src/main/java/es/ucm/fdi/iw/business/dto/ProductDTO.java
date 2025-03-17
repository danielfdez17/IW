package es.ucm.fdi.iw.business.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@Builder
public class ProductDTO {

    private long id;
    private long creadorUserId;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private boolean enabled;
    private String rutaImagen;
    private String descripcion;
    private double precio;
    private String nombre;
}
