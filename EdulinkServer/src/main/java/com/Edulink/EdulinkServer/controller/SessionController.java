package com.Edulink.EdulinkServer.controller;

import com.Edulink.EdulinkServer.model.Session;
import com.Edulink.EdulinkServer.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/sessions")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @PostMapping("/create")
    public ResponseEntity<?> createSession(
            @RequestParam Long userId,
            @RequestParam String topic,
            @RequestParam int durationInMinutes,
            @RequestParam boolean allowAnyoneToJoin,
            @RequestParam(required = false) Long classroomId
    ) {
        try {
            Session session = sessionService.startSession(userId, topic, durationInMinutes, allowAnyoneToJoin, classroomId);
            return ResponseEntity.ok(session);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }


    @GetMapping("/sessions")
    public ResponseEntity<?> getStudentSessions(@RequestParam String studentEmail){
        try {
            List<Session> sessions = sessionService.getStudentSession(studentEmail);
            if (sessions.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No sessions found for this student");
            }
            return ResponseEntity.ok(sessions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }



}