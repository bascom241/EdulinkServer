package com.Edulink.EdulinkServer.controller;


import com.Edulink.EdulinkServer.dto.notification.NotificationDTO;
import com.Edulink.EdulinkServer.model.Notification;
import com.Edulink.EdulinkServer.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/notifications")


public class NotificationRestController {
    @Autowired
    private NotificationRepository notificationRepository;


    @GetMapping("/teacher/{teacherId}")
    public List<NotificationDTO> getAllTeacherNotifications(@PathVariable(name = "teacherId") Long teacherId){
        try {
            return notificationRepository.findByTeacher_UserIdOrderByTimestampDesc(teacherId)
                    .stream()
                    .map(NotificationDTO::new)
                    .toList();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/teacher/{teacherId}/read")
    public ResponseEntity<?> markAllAsRead(@PathVariable Long teacherId){
        try {
            List<Notification> unreadNotifications = notificationRepository.findByTeacher_UserIdAndIsReadFalse(teacherId);

            unreadNotifications.forEach(notification -> notification.setRead(true));

            notificationRepository.saveAll(unreadNotifications);
            return ResponseEntity.ok().body("All Notifications mark as read");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error marking notifications as read");
        }
    }

    @DeleteMapping("/teacher/{teacherId}/{notificationId}/delete")
    public ResponseEntity<?> deleteNotification(@PathVariable Long teacherId, @PathVariable Long notificationId){
        // find all notifications that belong to the teacher
        // find single notification in the teacher notification
        // delete the notification entirely from the database
        try {
            List<Notification> teacherNotifications = notificationRepository.findByTeacher_UserIdAndIsReadTrue(teacherId);
            if(teacherNotifications.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No notification found");
            }
            Notification readNotification = notificationRepository.findById(notificationId).orElseThrow(()-> new RuntimeException("No Read not found"));

            notificationRepository.delete(readNotification);

            return ResponseEntity.status(HttpStatus.OK).body("Notification Deleted");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/teacher/{teacherId}/delete-all")
    public ResponseEntity<?> deleteAllTeacherNotifications(@PathVariable Long teacherId){
        try {
            List<Notification> teacherNotifications = notificationRepository.findByTeacher_UserIdAndIsReadTrue(teacherId);
            notificationRepository.deleteAll(teacherNotifications);
            return ResponseEntity.status(HttpStatus.OK).body("Notifications Deleted");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
