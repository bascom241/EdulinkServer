package com.Edulink.EdulinkServer.service;

import com.Edulink.EdulinkServer.dao.UserRepository;
import com.Edulink.EdulinkServer.model.Session;
import com.Edulink.EdulinkServer.model.User;
import com.Edulink.EdulinkServer.repository.SessionRepository;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SctaService {

    private static final Logger log = LoggerFactory.getLogger(SctaService.class);

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Scheduled(cron = "0 * * * * ?")
    public void incrementSctaPoint() {
        log.info("Scheduled task started: incrementSctaPoint");

        List<Session> endedSessions = sessionRepository.findByStatus("ENDED");
        log.info("Found {} ended sessions", endedSessions.size());

        if (endedSessions.isEmpty()) {
            log.info("No sessions to process, exiting");
            return;
        }

        Set<User> teachersToUpdate = new HashSet<>();

        for (Session session : endedSessions) {
            User creator = session.getCreator();
            if (creator != null && creator.isTeacher()) {
                int oldPoints = creator.getSctaPoints();
                creator.setSctaPoints(oldPoints + 2);
                teachersToUpdate.add(creator);
                log.info("Updated teacher {} SCTA points from {} -> {}", creator.getUserId(), oldPoints, creator.getSctaPoints());
            } else {
                log.info("Session {} has no valid teacher creator", session.getSessionId());
            }
        }

        if (!teachersToUpdate.isEmpty()) {
            userRepository.saveAll(teachersToUpdate);
            log.info("Saved {} updated teachers", teachersToUpdate.size());
        }

        endedSessions.forEach(s -> {
            log.info("Marking session {} as PROCESSED", s.getSessionId());
            s.setStatus("PROCESSED");
        });

        sessionRepository.saveAll(endedSessions);
        log.info("Saved {} sessions as PROCESSED", endedSessions.size());

        log.info("Scheduled task finished: incrementSctaPoint");
    }
}
