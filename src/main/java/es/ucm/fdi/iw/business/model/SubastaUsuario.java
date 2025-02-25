package es.ucm.fdi.iw.business.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
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
    private User user;

    private Subasta subasta;
}
