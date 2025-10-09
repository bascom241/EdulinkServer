package com.Edulink.EdulinkServer.controller;

import com.Edulink.EdulinkServer.dto.SessionDTO;
import com.Edulink.EdulinkServer.dto.TeacherSessionDto;
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
            @RequestParam String  sessionPassword,
            @RequestParam boolean requirePassword,
            @RequestParam(required = false) Long classroomId
    ) {
        try {
            Session session = sessionService.startSession(userId, topic, durationInMinutes, allowAnyoneToJoin, sessionPassword, requirePassword,  classroomId);
            return ResponseEntity.ok(session);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }


    @GetMapping("/sessions")
    public ResponseEntity<?> getStudentSessions(@RequestParam String studentEmail){
        try {
            List<SessionDTO> sessions = sessionService.getStudentSession(studentEmail);
            if (sessions.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No sessions found for this student");
            }
            return ResponseEntity.ok(sessions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/instructor-sessions")
    public ResponseEntity<?> getInstructorSessions(@RequestParam String teacherEmail){
        try {
            List<TeacherSessionDto> sessions = sessionService.getTeacherSession(teacherEmail);

            if(sessions.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No sessions for this Instructor");
            }
            return ResponseEntity.ok(sessions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/all-instructor-sessions")
    public ResponseEntity<?> getAllInstructorSessions(@RequestParam String teacherEmail){
        try {
            List<TeacherSessionDto> sessions = sessionService.getAllTeacherSessions(teacherEmail);

            if(sessions.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No sessions for this Instructor");
            }
            return ResponseEntity.ok(sessions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    

    @GetMapping("/instructor/{sessionId}")
    public ResponseEntity<?> getSingleSession(@PathVariable(name = "sessionId") Long sessionId , @RequestParam String teacherEmail){
        try {
            TeacherSessionDto teacherSessionDto = sessionService.getTeacherSingleSession(teacherEmail, sessionId);
            if(teacherSessionDto == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Session Cant Be found Or Ended");
            }
            return ResponseEntity.status(HttpStatus.OK).body(teacherSessionDto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }




}