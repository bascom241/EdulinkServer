package com.Edulink.EdulinkServer.service;

import com.Edulink.EdulinkServer.dao.UserRepository;
import com.Edulink.EdulinkServer.model.Session;
import com.Edulink.EdulinkServer.model.User;
import com.Edulink.EdulinkServer.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SctaService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;



    // 5 minutes before midnight
    @Scheduled(cron = "0 55 23 * * ?")
    public void incrementSctaPoint(){
        List<Session> endedSessions = sessionRepository.findByStatus("ENDED");

        if(endedSessions.isEmpty()) return ;
        Set<User> teachersToUpdate = new HashSet<>();

        for(Session session : endedSessions){
            User creator = session.getCreator();
            if(creator != null && creator.isTeacher()){
                creator.setSctaPoints(creator.getSctaPoints() + 2 );
                teachersToUpdate.add(creator);
            }
        }

        userRepository.saveAll(teachersToUpdate);

        endedSessions.forEach(s->s.setStatus("PROCESSED"));
        sessionRepository.saveAll(endedSessions);


    }
}
