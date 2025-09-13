package com.Edulink.EdulinkServer.service;


import com.Edulink.EdulinkServer.dao.UserRepository;
import com.Edulink.EdulinkServer.dto.SessionDTO;
import com.Edulink.EdulinkServer.dto.TeacherSessionDto;
import com.Edulink.EdulinkServer.model.Classroom;
import com.Edulink.EdulinkServer.model.Session;
import com.Edulink.EdulinkServer.model.StudentInfo;
import com.Edulink.EdulinkServer.model.User;
import com.Edulink.EdulinkServer.repository.ClassRepository;
import com.Edulink.EdulinkServer.repository.SessionRepository;
import com.Edulink.EdulinkServer.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EmailService emailService;

    public Session startSession(Long userId , String topic , int durationInMinutes , boolean allowAnyOneToJoin , String sessionPassword, boolean requirePassword,   Long classRoomId){
        User creator = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Creator not found"));


        List<StudentInfo> studentInfo = null;
        Classroom classroom = null;


        if(classRoomId != null){
            classroom = classRepository.findById(classRoomId)
                    .orElseThrow(() -> new RuntimeException("Classroom not found"));

            studentInfo = classroom.getStudents();
            if (classroom.getOwner() == null || !classroom.getOwner().getUserId().equals(userId)) {
                throw new RuntimeException("Only classroom owner can create session for this classroom");
            }

            if(!classroom.isSessionOngoing()){
                classroom.setSessionOngoing(true);
                classRepository.save(classroom);
            }


        }

        System.out.println("Classroom owner: " + (classroom.getOwner() != null ? classroom.getOwner().getUserId() : "null"));

        Session session = new Session();

        session.setCreator(creator);
        session.setTopic(topic);
        session.setDurationInMinutes(durationInMinutes);
        session.setAllowAnyoneToJoin(allowAnyOneToJoin);
        session.setClassroom(classroom);

        if(requirePassword){
            session.setRequirePassword(true);
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(8);
            String encodedPassword = encoder.encode(sessionPassword);
            session.setSessionPassword(encodedPassword);
        }else {
            session.setSessionPassword(null);
            session.setRequirePassword(false);
        }

       LocalDateTime start = LocalDateTime.now();
       LocalDateTime end = start.plusMinutes(durationInMinutes);


        session.setStartTime(start);
        session.setEndTime(end);
        session.setStatus("STARTED");

        String deliveryModel = classroom.getClassDeliveryModel();

        if(Objects.equals(classroom.getClassDeliveryModel(), "Physical")){
            session.setSessionClassLocation(classroom.getClassLocation());
        } else if(Objects.equals(classroom.getClassLocation(), "Online")){
            session.setSessionClassLocation(classroom.getClassLocation());
        }else {
            throw new IllegalArgumentException("Unknown Delivery Model: " +  deliveryModel);
        }

        emailService.sendSessionStartedNotifications(session,studentInfo,session.getCreator().getEmail());
        return sessionRepository.save(session);

    }

    public List<SessionDTO> getStudentSession(String email) {
        List<StudentInfo> students = studentRepository.findByEmail(email);



        if (students == null || students.isEmpty()) {
            throw new RuntimeException(email + " is not found in the list of this session");
        }

        // Collect all classrooms from all students with this email
        List<Classroom> classrooms = students.stream()
                .flatMap(student -> student.getClassrooms().stream())
                .distinct() // avoid duplicates
                .toList();

        if (classrooms.isEmpty()) {
            return List.of();
        }

        // Get all sessions for these classrooms
        return classrooms.stream()
                .flatMap(classroom -> sessionRepository.findDTOByClassroom(classroom).stream())
                .toList();
    }

    public void endSession() {

    } // Todo
    public List<TeacherSessionDto> getTeacherSession(String email) {
        // Find the instructor
        User instructor = userRepository.findByEmail(email);

        if (instructor != null && instructor.isStudent()) {
            throw new RuntimeException("Sessions of instructor can only be queried");
        }

        // Fetch all sessions created by instructor
        List<TeacherSessionDto> sessions = sessionRepository.findTeacherSessions(instructor);

        // Filter to only "ONGOING" sessions
        LocalDateTime now = LocalDateTime.now();

        return sessions.stream()
                .filter(s -> "ONGOING".equalsIgnoreCase(s.getStatus())) // only ongoing
//                .filter(s -> s.getStartTime() != null && s.getEndTime() != null)
//                .filter(s -> !now.isBefore(s.getStartTime())   // already started
//                        && !now.isAfter(s.getEndTime()))     // not ended yet
                .toList();
    }
    public List<TeacherSessionDto> getAllTeacherSessions(String email){
        User instructor = userRepository.findByEmail(email);

        if (instructor != null && instructor.isStudent()) {
            throw new RuntimeException("Sessions of instructor can only be queried");
        }

        return sessionRepository.findTeacherSessions(instructor);
    }



}
