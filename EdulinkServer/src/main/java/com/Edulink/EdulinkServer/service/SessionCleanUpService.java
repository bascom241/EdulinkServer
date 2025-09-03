package com.Edulink.EdulinkServer.service;

import com.Edulink.EdulinkServer.model.Session;
import com.Edulink.EdulinkServer.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SessionCleanUpService {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private SessionRepository sessionRepository;

    @Scheduled(fixedRate = 5 * 60 * 60 * 1000)
    public void markSessionsAsEnded() {
        List<Session> expiredSessions = sessionRepository.findByEndTimeBefore(LocalDateTime.now());
        for (Session session : expiredSessions) {
            if (!"ENDED".equals(session.getStatus())) {
                session.setStatus("ENDED");
                sessionRepository.save(session);
            }
        }

        System.out.println("Marked " + expiredSessions.size() + " sessions as ENDED");

    }

    @Scheduled(cron = "0 0 0 * * ?") // midnight
    public void deleteExpiredSessionsAtMidnight() {
        List<Session> expiredSessions = sessionRepository.findByEndTimeBefore(LocalDateTime.now());
        sessionRepository.deleteAll(expiredSessions);
        System.out.println("Deleted " + expiredSessions.size() + " expired sessions");
    }

}
