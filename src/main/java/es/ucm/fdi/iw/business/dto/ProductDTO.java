package es.ucm.fdi.iw.business.dto;


import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ProductDTO {

    private long id;
    private BigDecimal precio;
    private String nombre;
    private String descripcion;

}
