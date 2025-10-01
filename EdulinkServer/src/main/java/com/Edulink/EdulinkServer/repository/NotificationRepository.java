package com.Edulink.EdulinkServer.repository;

import com.Edulink.EdulinkServer.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT n FROM Notification n WHERE n.classroom.id = :classroomId ORDER BY n.timestamp ASC")
    List<Notification> findByClassroomIdOrdered(Long classroomId);

    List<Notification> findByTeacher_UserIdOrderByTimestampDesc(Long teacherId);
    List<Notification> findByTeacher_UserIdAndIsReadFalse(Long teacherId);
    List<Notification> findByTeacher_UserIdAndIsReadTrue(Long teacherId);
}
