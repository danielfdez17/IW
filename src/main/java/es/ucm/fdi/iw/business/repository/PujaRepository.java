package es.ucm.fdi.iw.business.repository;

import es.ucm.fdi.iw.business.model.Puja;
import es.ucm.fdi.iw.business.model.PujaEmbed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import java.util.List;

@Repository
public interface PujaRepository extends JpaRepository<Puja, PujaEmbed> {

    @Query("""
            SELECT p FROM Puja p
            WHERE p.user.id = :usuarioId AND p.subasta.id = :subastaId
            """)
    Optional<Puja> findById_UsuarioIdAndId_SubastaId(@Param("usuarioId") Long usuarioId,
            @Param("subastaId") Long subastaId);



    @Query("""
        SELECT p FROM Puja p
        WHERE p.user.id = :usuarioId
        """)
        List<Puja> findAllByUserId(@Param("usuarioId") Long usuarioId);


}
