package es.ucm.fdi.iw.business.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.ucm.fdi.iw.business.model.Subasta;

@Repository
public interface SubastaRepository extends JpaRepository<Subasta, Long> {
    
    @Query("SELECT s FROM Subasta s")
    List<Subasta> getAllSubastas();

    @Query("SELECT s FROM Subasta s JOIN s.pujas p WHERE p.user.id = :userId")
    List<Subasta> findSubastasByUserId(@Param("userId") Long userId);
}
