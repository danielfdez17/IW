package es.ucm.fdi.iw.business.model;

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
}
