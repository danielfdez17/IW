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
    private double precioActual;
    private double precio;
    private double precioInicial;
    private String nombre;
    //private boolean favorito;
    private double dineroPujado; 
    private boolean usuarioHaPujado; 
    
    
    public boolean isActiva() {
        return fechaFin != null && fechaFin.isAfter(LocalDateTime.now());
    }
}
