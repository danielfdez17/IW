package es.ucm.fdi.iw.business.model;

import java.time.LocalDateTime;
import java.util.List;

import es.ucm.fdi.iw.business.enums.EstadoSubasta;
import es.ucm.fdi.iw.business.enums.RepartoSubasta;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(indexes = {
    @Index(name = "idx_subasta_estado", columnList = "estado")
})
public class Subasta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    @Column(columnDefinition = "boolean default true")
    private boolean enabled;

    private String rutaImagen;
    private double precioInicial;

    private String descripcion;
    private String nombre;

    private double precioActual;
    private double precio;
    private String maximoPujador;

    @OneToMany(mappedBy = "subasta")
    private List<Puja> pujas;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSubasta estado;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User creador;
    
    @ManyToOne
    @JoinColumn(name = "ganador_id", nullable = true)
    private User ganador;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RepartoSubasta repartoSubasta = RepartoSubasta.PENDIENTE_DE_TERMINAR_SUBASTA;

    private String comentarioGanador;
    private Byte valoracionGanador;
}