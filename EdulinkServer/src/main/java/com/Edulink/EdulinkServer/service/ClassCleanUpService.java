package com.Edulink.EdulinkServer.service;


import com.Edulink.EdulinkServer.model.Classroom;
import com.Edulink.EdulinkServer.repository.ClassRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClassCleanUpService {

    private final ClassRepository classRepository;


    public ClassCleanUpService(ClassRepository classRepository) {
        this.classRepository = classRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteExpiredClassRoom(){
        List<Classroom> expired = classRepository.findByExpiresAtBefore(LocalDateTime.now());
        classRepository.deleteAll(expired);
    }
}
