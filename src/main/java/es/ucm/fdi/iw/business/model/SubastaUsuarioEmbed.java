package es.ucm.fdi.iw.business.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class SubastaUsuarioEmbed implements Serializable {
    @Column(name = "user_id")
    private long usuarioId;
    
    @Column(name = "subasta_id")
    private long subastaId;

}
