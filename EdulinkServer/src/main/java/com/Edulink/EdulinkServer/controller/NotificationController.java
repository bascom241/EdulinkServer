package com.Edulink.EdulinkServer.controller;

import com.Edulink.EdulinkServer.dao.UserRepository;
import com.Edulink.EdulinkServer.dto.notification.NotificationMessage;
import com.Edulink.EdulinkServer.model.Classroom;
import com.Edulink.EdulinkServer.model.Notification;
import com.Edulink.EdulinkServer.model.User;
import com.Edulink.EdulinkServer.repository.ClassRepository;
import com.Edulink.EdulinkServer.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/api/notifications")
public class NotificationController {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;



    // ---------------- Teacher broadcasts a message ----------------

    @MessageMapping("/notify")
    public void sendNotification(NotificationMessage notificationMessage){
        Classroom classroom = classRepository.findById(notificationMessage.getClassroomId()).orElseThrow(()-> new RuntimeException("Class Id not Found"));

      if(!classroom.getOwner().getUserId().equals(notificationMessage.getTeacherId())){
          throw new RuntimeException("You are not authorized to broadcast messages");
      }

        Notification notification = new Notification();
        notification.setContent(notificationMessage.getContent());
        notification.setTimestamp(LocalDateTime.now());
        notification.setClassroom(classroom);
        notification.setTeacher(classroom.getOwner());
        notification.setType("TEACHER_MESSAGE");

        notificationRepository.save(notification);

        // Send to students subscribed to this classroom
        String destination = "/topic/classroom." + notificationMessage.getClassroomId();
        simpMessagingTemplate.convertAndSend(destination, notificationMessage);

    }
}
