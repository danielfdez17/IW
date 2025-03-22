package es.ucm.fdi.iw.business.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import es.ucm.fdi.iw.business.model.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("""
        SELECT m FROM Message m 
        WHERE m.sender.id = :userId OR m.recipient.id = :userId 
        ORDER BY m.dateSent ASC
    """)
    List<Message> findMessagesByUserId(Long userId);
}
