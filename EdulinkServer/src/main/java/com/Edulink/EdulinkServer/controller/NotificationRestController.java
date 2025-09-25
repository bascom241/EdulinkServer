package com.Edulink.EdulinkServer.controller;


import com.Edulink.EdulinkServer.dto.notification.NotificationDTO;
import com.Edulink.EdulinkServer.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
