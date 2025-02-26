package es.ucm.fdi.iw.business.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.ucm.fdi.iw.business.model.Subasta;

@Repository 
public interface SubastaRepository extends JpaRepository<Subasta, Long> {
    
    List<Subasta> getAll();
}
