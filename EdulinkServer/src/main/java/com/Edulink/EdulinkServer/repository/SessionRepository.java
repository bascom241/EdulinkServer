package com.Edulink.EdulinkServer.repository;


import com.Edulink.EdulinkServer.dto.SessionDTO;
import com.Edulink.EdulinkServer.model.Classroom;
import com.Edulink.EdulinkServer.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    @Query("SELECT new com.Edulink.EdulinkServer.dto.SessionDTO(" +
            "s.sessionId, s.topic, s.status, s.durationInMinutes, " +
            "s.startTime, s.endTime, s.allowAnyoneToJoin, " +
            "u.firstName, u.lastName) " +
            "FROM Session s JOIN s.creator u " +
            "WHERE s.classroom = :classroom")
    List<SessionDTO> findDTOByClassroom(@Param("classroom") Classroom classroom);

    Session findByStatus(String status);

    List<Session> findByEndTimeBefore(LocalDateTime localDateTime);
}

