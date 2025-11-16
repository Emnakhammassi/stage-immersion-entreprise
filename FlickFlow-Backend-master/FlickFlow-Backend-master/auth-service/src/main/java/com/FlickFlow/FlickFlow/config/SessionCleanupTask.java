package com.FlickFlow.FlickFlow.config;

import com.FlickFlow.FlickFlow.user.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SessionCleanupTask {

    private final SessionRepository sessionRepository;

    @Autowired
    public SessionCleanupTask(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Scheduled(cron = "0 0 * * * ?") // Run every hour
    public void cleanExpiredSessions() {
        sessionRepository.deleteExpiredSessions();
    }
}
