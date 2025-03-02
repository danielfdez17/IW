package es.ucm.fdi.iw.business.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.ucm.fdi.iw.business.model.Subasta;

@Repository
public interface SubastaRepository extends JpaRepository<Subasta, Long> {

}
