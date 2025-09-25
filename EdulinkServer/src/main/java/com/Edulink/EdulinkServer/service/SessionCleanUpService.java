package com.Edulink.EdulinkServer.service;

import com.Edulink.EdulinkServer.dao.UserRepository;
import com.Edulink.EdulinkServer.model.Classroom;
import com.Edulink.EdulinkServer.model.Session;
import com.Edulink.EdulinkServer.model.User;
import com.Edulink.EdulinkServer.repository.ClassRepository;
import com.Edulink.EdulinkServer.repository.SessionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SessionCleanUpService {



    @Autowired
    private SessionService sessionService;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClassRepository classRepository;
    // Every midnight
    @Transactional
    @Scheduled(cron = "0 * * * * ?")

    public void markSessionsAsEnded() {
        List<Session> expiredSessions = sessionRepository.findByEndTimeBefore(LocalDateTime.now());
        for (Session session : expiredSessions) {
            if (!"ENDED".equals(session.getStatus()) && !"PROCESSED".equals(session.getStatus())) {
                session.setStatus("ENDED");

                Classroom classroom = session.getClassroom();
                if(classroom != null && classroom.isSessionOngoing()){
                    classroom.setSessionOngoing(false);
                    classRepository.save(classroom);
                }

                sessionRepository.save(session);
            }
        }

        System.out.println("Marked " + expiredSessions.size() + " sessions as ENDED");

    }

    // Every midnight
//    @Scheduled(cron = "0 0 0 * * ?") // midnight
//    public void deleteExpiredSessionsAtMidnight() {
//        List<Session> expiredSessions = sessionRepository.findByEndTimeBefore(LocalDateTime.now());
//        sessionRepository.deleteAll(expiredSessions);
//        System.out.println("Deleted " + expiredSessions.size() + " expired sessions");
//    }

    // Every One hour
//    @Transactional
//    @Scheduled(cron = "0 * * * * ?")
//    public void incrementSessions (){
//        List<Session> ongoingSessions = sessionRepository.findByStatus("STARTED");
//
//        if(ongoingSessions.isEmpty()) return ;
//        Set<User> teachersToUpdate = new HashSet<>();
//
//        for (Session session : ongoingSessions){
//            User creator = session.getCreator();
//            if(creator != null && creator.isTeacher()){
//                creator.setNoOfSessions(creator.getNoOfSessions() + 1 );
//                teachersToUpdate.add(creator);
//            }
//        }
//
//        userRepository.saveAll(teachersToUpdate);
//        ongoingSessions.forEach(session -> session.setStatus("ONGOING"));
//        sessionRepository.saveAll(ongoingSessions);
//
//
//    }


}
