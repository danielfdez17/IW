package es.ucm.fdi.iw.business.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ArchivedDTO {
    private String id;
    private String nombreProducto;
    private String imagenUrl;
    private String precio;
    private String usuario;
    private String fecha;
}
