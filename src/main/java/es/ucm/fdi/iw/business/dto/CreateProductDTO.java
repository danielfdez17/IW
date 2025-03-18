package es.ucm.fdi.iw.business.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CreateProductDTO {
    private String fechaInicio;
    private String fechaFin;
    private String descripcion;
    private double precio;
    private String nombre;
}
