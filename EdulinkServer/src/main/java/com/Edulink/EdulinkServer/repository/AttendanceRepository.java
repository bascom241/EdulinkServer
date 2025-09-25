package com.Edulink.EdulinkServer.repository;

import com.Edulink.EdulinkServer.model.Attendance;
import com.Edulink.EdulinkServer.model.Session;
import com.Edulink.EdulinkServer.model.StudentInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance , Long> {
    List<Attendance> findByStudentInfo(StudentInfo studentInfo);

    Optional<Attendance> findByStudentInfoAndSession(StudentInfo student, Session session);
}
