//package com.Edulink.EdulinkServer.controller;
//
//
//import com.Edulink.EdulinkServer.model.Notification;
//import com.Edulink.EdulinkServer.repository.NotificationRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/notifications")
//public class NotificationRestController {
//
//
//    @Autowired
//    private NotificationRepository notificationRepository;
//
//
//    @GetMapping("/{classroomId}")
//    public List<Notification> getNotifications(@PathVariable Long classroomId) {
//        return notificationRepository.findByClassroomIdOrdered(classroomId);
//    }
//}
