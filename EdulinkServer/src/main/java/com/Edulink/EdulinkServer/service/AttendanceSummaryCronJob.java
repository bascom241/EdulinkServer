package com.Edulink.EdulinkServer.service;

import com.Edulink.EdulinkServer.model.Attendance;
import com.Edulink.EdulinkServer.model.StudentInfo;
import com.Edulink.EdulinkServer.repository.AttendanceRepository;
import com.Edulink.EdulinkServer.repository.StudentRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttendanceSummaryCronJob {


    private StudentRepository studentRepository;
    private AttendanceRepository attendanceRepository;

    @Scheduled(cron = "0 0 0 * * ?") // every midnight
    public void updateAttendanceSummaries() {
        List<StudentInfo> students = studentRepository.findAll();

        for (StudentInfo student : students) {
            List<Attendance> records = attendanceRepository.findByStudentInfo(student);

            if (records.isEmpty()) {
                student.setAttendanceRate(0.0);
                student.setStatus("inactive");
                studentRepository.save(student);
                continue;
            }

            long total = records.size();
            long attended = records.stream().filter(Attendance::isPresent).count();
            double rate = (attended * 100.0) / total;

            student.setAttendanceRate(rate);
            student.setStatus(determineStatus(rate));
            studentRepository.save(student);
        }
    }

    private String determineStatus(double rate) {
        if (rate >= 75) return "active";
        else if (rate >= 50) return "pending";
        else return "inactive";
    }
}
