package com.Edulink.EdulinkServer.repository;

import com.Edulink.EdulinkServer.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
//    @Query("SELECT n FROM Notification n WHERE n.classroom.<exactFieldName> = :classroomId ORDER BY n.timestamp DESC")
//    List<Notification> findByClassroomIdOrdered(@Param("classroomId") Long classroomId);


}
