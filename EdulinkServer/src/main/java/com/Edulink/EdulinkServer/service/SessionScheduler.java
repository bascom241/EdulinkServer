package com.Edulink.EdulinkServer.service;

import com.Edulink.EdulinkServer.dao.UserRepository;
import com.Edulink.EdulinkServer.model.Session;
import com.Edulink.EdulinkServer.model.User;
import com.Edulink.EdulinkServer.repository.SessionRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SessionScheduler {

    private static final Logger log = LoggerFactory.getLogger(SessionScheduler.class);

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Scheduled(cron = "0 0 * * * ?")
 // every minute
    public void incrementSessions() {
        log.info(">>> incrementSessions job triggered at {}", LocalDateTime.now());

        List<Session> ongoingSessions = sessionRepository.findByStatus("STARTED");
        log.info("Found {} sessions with status STARTED", ongoingSessions.size());

        if (ongoingSessions.isEmpty()) {
            log.info("No sessions to process. Exiting...");
            return;
        }

        Set<User> teachersToUpdate = new HashSet<>();

        for (Session session : ongoingSessions) {
            log.info("Processing session: id={}, status={}, startTime={}",
                    session.getSessionId(), session.getStatus(), session.getStartTime());

            User creator = session.getCreator();
            if (creator != null) {
                log.info("Session creator: id={}, name={}, isTeacher={}",
                        creator.getUserId(), creator.getFirstName(), creator.isTeacher());

                if (creator.isTeacher()) {
                    Integer currentSessions = creator.getNoOfSessions();
                    if (currentSessions == null) currentSessions = 0;

                    creator.setNoOfSessions(currentSessions + 1);
                    teachersToUpdate.add(creator);

                    log.info("Updated teacher {} -> noOfSessions={}", creator.getUserId(), creator.getNoOfSessions());
                }
            } else {
                log.warn("Session {} has no creator!", session.getCreator().getUserId());
            }
        }

        if (!teachersToUpdate.isEmpty()) {
            log.info("Saving {} teachers...", teachersToUpdate.size());
            userRepository.saveAll(teachersToUpdate);
        } else {
            log.info("No teachers to update.");
        }

        ongoingSessions.forEach(session -> {
            session.setStatus("ONGOING");
            log.info("Updated session {} status -> ONGOING", session.getSessionId());
        });

        sessionRepository.saveAll(ongoingSessions);
        log.info("Saved {} sessions with updated status", ongoingSessions.size());

        log.info(">>> incrementSessions job completed at {}", LocalDateTime.now());
    }
}
