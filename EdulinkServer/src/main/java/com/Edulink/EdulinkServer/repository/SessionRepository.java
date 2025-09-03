package com.Edulink.EdulinkServer.repository;


import com.Edulink.EdulinkServer.model.Classroom;
import com.Edulink.EdulinkServer.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
     Session findByStatus(String status );
    List<Session> findByEndTimeBefore(LocalDateTime localDateTime);
    List<Session> findByClassroom(Classroom classroom);
}
