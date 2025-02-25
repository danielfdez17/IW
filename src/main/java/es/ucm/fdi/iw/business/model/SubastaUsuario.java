package es.ucm.fdi.iw.business.model;

import java.math.BigDecimal;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SubastaUsuario {
    
    @EmbeddedId
    private SubastaUsuarioEmbed id;

    @ManyToOne
    @MapsId("usuarioId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne
    @MapsId("subastaId")
    @JoinColumn(name = "subasta_id", nullable = false)
    private Subasta subasta;

    private BigDecimal dineroPujado; 
}
