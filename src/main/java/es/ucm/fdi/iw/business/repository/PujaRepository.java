package es.ucm.fdi.iw.business.repository;

import es.ucm.fdi.iw.business.model.Puja;
import es.ucm.fdi.iw.business.model.PujaEmbed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PujaRepository extends JpaRepository<Puja, PujaEmbed> {
    
    Optional<Puja> findById_UsuarioIdAndId_SubastaId(Long usuarioId, Long subastaId);
}
