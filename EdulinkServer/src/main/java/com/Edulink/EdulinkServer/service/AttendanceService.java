package com.Edulink.EdulinkServer.service;

import com.Edulink.EdulinkServer.model.Attendance;
import com.Edulink.EdulinkServer.model.Session;
import com.Edulink.EdulinkServer.model.StudentInfo;
import com.Edulink.EdulinkServer.repository.AttendanceRepository;
import com.Edulink.EdulinkServer.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private StudentRepository studentRepository;

    public void createAttendanceForSession(Session session){
        List<StudentInfo> students = session.getClassroom().getStudents();
        for(StudentInfo studentInfo : students){
            Attendance attendance = new Attendance();
            attendance.setPresent(false);
            attendance.setStudentInfo(studentInfo);
            attendance.setSession(session);
            attendanceRepository.save(attendance);
        }
    }

    public void markAttendance (Long studentId , Session session, boolean isPresent){
        StudentInfo student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Attendance attendance = attendanceRepository.findByStudentInfoAndSession(student, session)
                .orElseThrow(() -> new RuntimeException("Attendance record not found"));

        attendance.setPresent(isPresent);
        attendanceRepository.save(attendance);
    }
}
