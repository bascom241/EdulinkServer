package com.Edulink.EdulinkServer.controller;
import com.Edulink.EdulinkServer.dao.UserRepository;
import com.Edulink.EdulinkServer.dto.notification.NotificationMessage;
import com.Edulink.EdulinkServer.model.Classroom;
import com.Edulink.EdulinkServer.model.Notification;
import com.Edulink.EdulinkServer.repository.ClassRepository;
import com.Edulink.EdulinkServer.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class NotificationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private NotificationRepository notificationRepository;

    @MessageMapping("/notify")
    public void sendNotification (NotificationMessage notificationMessage){
        Classroom classroom = classRepository.findById(notificationMessage.getClassroomId())
                .orElseThrow(() -> new RuntimeException("Classroom not found"));

        if(!classroom.getOwner().getUserId().equals(notificationMessage.getTeacherId())){
            throw new RuntimeException("Your are not authorized to broadcast Messages");
        }

        Notification notification = new Notification();
        notification.setContent(notificationMessage.getContent());
        notification.setTimestamp(LocalDateTime.now());
        notification.setClassroom(classroom);

        notificationRepository.save(notification);

        String destination = "/topic/classroom." + notificationMessage.getClassroomId();
        simpMessagingTemplate.convertAndSend(destination,notificationMessage);


    }

}
