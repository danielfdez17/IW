package es.ucm.fdi.iw.business.repository;

import es.ucm.fdi.iw.business.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);

    @Query("""
            SELECT u FROM User u
            WHERE u.username = :username
            """)
    Optional<User> findByUsername(@Param("username") String username);

    @Query("""
            SELECT DISTINCT u
            FROM User u
            WHERE u.id IN (
                SELECT CASE
                    WHEN m.sender.id = :userId THEN m.recipient.id
                    ELSE m.sender.id
                END
                FROM Message m
                WHERE :userId IN (m.sender.id, m.recipient.id)
            )
            """)
    List<User> findChatPartners(Long userId);
}
