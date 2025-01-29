package es.ucm.fdi.iw.dto;

public class AutorDTO {

    private String nombre, descripcion;

    public AutorDTO(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
