package es.ucm.fdi.iw.business.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Subasta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    @Column(columnDefinition = "boolean default true")
    private boolean enabled;

    private String rutaImagen;

    private BigDecimal precio;

    private String descripcion;
    private String nombre;
}
