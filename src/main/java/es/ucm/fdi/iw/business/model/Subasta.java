package es.ucm.fdi.iw.business.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import es.ucm.fdi.iw.business.dto.ProductDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Subasta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    @Column(columnDefinition = "boolean default true")
    private boolean enabled;

    private String rutaImagen;

    private double precio;

    private String descripcion;
    private String nombre;

    @OneToMany(mappedBy = "subasta")
    private List<Puja> pujas;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User creador;

    // public ProductDTO toDTO() {
    // return ProductDTO.builder()
    // .id(id)
    // .precio(precio)
    // .descripcion(descripcion)
    // .nombre(nombre)
    // .build();
    // }

}
