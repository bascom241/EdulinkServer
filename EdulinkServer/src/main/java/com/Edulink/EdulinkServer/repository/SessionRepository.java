package com.Edulink.EdulinkServer.repository;


import com.Edulink.EdulinkServer.dto.SessionDTO;
import com.Edulink.EdulinkServer.dto.TeacherSessionDto;
import com.Edulink.EdulinkServer.model.Classroom;
import com.Edulink.EdulinkServer.model.Session;
import com.Edulink.EdulinkServer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    @Query("SELECT new com.Edulink.EdulinkServer.dto.SessionDTO(" +
            "s.sessionId, s.topic, s.status, s.durationInMinutes, " +
            "s.startTime, s.endTime, s.allowAnyoneToJoin, " +
            "u.firstName, u.lastName) " +
            "FROM Session s JOIN s.creator u " +
            "WHERE s.classroom = :classroom")
    List<SessionDTO> findDTOByClassroom(@Param("classroom") Classroom classroom);

    @Query("SELECT new com.Edulink.EdulinkServer.dto.TeacherSessionDto(" +
            "s.allowAnyoneToJoin, " +
            "s.sessionId, " +
            "s.topic, " +
            "s.status, " +
            "s.durationInMinutes, " +
            "s.startTime, " +   // LocalDateTime
            "s.endTime, " +     // LocalDateTime
            "c.className) " +   // adjust to match your Classroom entity field
            "FROM Session s " +
            "JOIN s.classroom c " +
            "WHERE s.creator = :instructor")
    List<TeacherSessionDto> findTeacherSessions(@Param("instructor") User instructor);

    @Query("SELECT new com.Edulink.EdulinkServer.dto.TeacherSessionDto(" +
            "s.allowAnyoneToJoin, " +
            "s.sessionId, " +
            "s.topic, " +
            "s.status, " +
            "s.durationInMinutes, " +
            "s.startTime, " +
            "s.endTime, " +
            "c.className) " +
            "FROM Session s " +
            "JOIN s.classroom c " +
            "WHERE s.sessionId = :sessionId")
    Optional<TeacherSessionDto> findTeacherSessionDtoBySessionId(@Param("sessionId") Long sessionId);
    List<Session> findByStatus(String status);

    List<Session> findByCreator(User user);

    List<Session> findByEndTimeBefore(LocalDateTime localDateTime);
}

