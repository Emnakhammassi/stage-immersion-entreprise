package com.FlickFlow.FlickFlow.user.repository;
import com.FlickFlow.FlickFlow.user.entity.Session;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
public interface SessionRepository extends JpaRepository<Session,Long> {
    Optional<Session> findByToken(String token);
    List<Session> findByUsernameAndRevokedFalse(String username);

    @Query("DELETE FROM Session s WHERE s.expiresAt < CURRENT_TIMESTAMP")
    void deleteExpiredSessions();
    void deleteByToken(String token);
}
