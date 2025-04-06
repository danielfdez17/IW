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
        WHERE 
        (m.sender.id = :userId1 AND m.recipient.id = :userId2) 
            OR 
        (m.sender.id = :userId2 AND m.recipient.id = :userId1) 
        ORDER BY m.dateSent ASC
    """)
    List<Message> findMessagesByUserId(Long userId1, Long userId2);
}
