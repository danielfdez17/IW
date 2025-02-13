package es.ucm.fdi.iw.business.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductDTO {

    private String nombre;
    private String descripcion;
    private double precio;

}
