package es.ucm.fdi.iw.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ObjetoDTO {

    private String nombre;
    private String descripcion;
    private double precio;

}
