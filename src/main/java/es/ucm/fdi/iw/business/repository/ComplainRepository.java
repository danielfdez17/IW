package es.ucm.fdi.iw.business.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.ucm.fdi.iw.business.model.Complain;

public interface ComplainRepository extends JpaRepository<Complain, Long> {

}
