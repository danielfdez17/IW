package es.ucm.fdi.iw.business.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    private long id;
    private long creadorUserId;
    private String creadorUsername;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private boolean enabled;
    private String rutaImagen;
    private String descripcion;
    private String nombre;
    private double precio;
    private double precioActual;
    private String maximoPujador;
    private double precioInicial;
    private double dineroPujado; 
    private boolean usuarioHaPujado; 
}