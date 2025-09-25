package com.Edulink.EdulinkServer.mapper;

import com.Edulink.EdulinkServer.dto.SessionDTO;
import com.Edulink.EdulinkServer.dto.StudentInfoDto;
import com.Edulink.EdulinkServer.dto.classroom.ClassroomResponseDto;
//import com.Edulink.EdulinkServer.dto.session.SessionDto;
//import com.Edulink.EdulinkServer.dto.student.StudentInfoDto;
import com.Edulink.EdulinkServer.model.Classroom;

import java.util.stream.Collectors;

public class ClassroomMapper {

    public static ClassroomResponseDto toDto(Classroom classroom) {
        ClassroomResponseDto dto = new ClassroomResponseDto();

        dto.setClassId(classroom.getClassId());
        dto.setClassName(classroom.getClassName());
        dto.setClassDescription(classroom.getClassDescription());
        dto.setClassroomPrice(classroom.getClassroomPrice());
        dto.setClassroomFull(classroom.isClassroomFull());

        dto.setClassDurationInDays(classroom.getClassDurationInDays());
        dto.setCreatedAt(classroom.getCreatedAt());
        dto.setExpiresAt(classroom.getExpiresAt());
        dto.setClassDeliveryModel(classroom.getClassDeliveryModel());
        dto.setClassLocation(classroom.getClassLocation());
        dto.setTargetAudience(classroom.getTargetAudience());
        dto.setClassCategory(classroom.getClassCategory());
        dto.setSessionOngoing(classroom.isSessionOngoing());

        dto.setResources(classroom.getResources());
        dto.setAssignments(classroom.getAssignments());
        dto.setTasks(classroom.getTasks());

        // ✅ Map Students inline
        dto.setStudents(
                classroom.getStudents().stream().map(student -> {
                    StudentInfoDto sDto = new StudentInfoDto();
                    sDto.setStudentId(student.getStudentId());
                    sDto.setFirstName(student.getFirstName());
                    sDto.setLastName(student.getLastName());
                    sDto.setEmail(student.getEmail());
                    sDto.setAttendanceRate(student.getAttendanceRate());
                    sDto.setJoinDate(student.getJoinDate());
                    sDto.setLastActive(student.getLastActive());
                    sDto.setProgress(student.getProgress());
                    sDto.setStatus(student.getStatus());

                    return sDto;
                }).collect(Collectors.toList())
        );

        // ✅ Map Sessions inline
        dto.setSessions(
                classroom.getSessions().stream().map(session -> {
                    SessionDTO sessDto = new SessionDTO();
                    sessDto.setSessionId(session.getSessionId());
                    sessDto.setTopic(session.getTopic());
                    sessDto.setCreatorFirstName(session.getCreator().getFirstName());
                    sessDto.setCreatorLastName(session.getCreator().getLastName());
                    sessDto.setStatus(session.getStatus());

                    sessDto.setDurationInMinutes(session.getDurationInMinutes());

                    return sessDto;
                }).collect(Collectors.toList())
        );

        dto.setNumberOfStudents(classroom.getStudents().size());
        dto.setNumberOfSessions(classroom.getSessions().size());
        dto.setNumberOfQuestions(classroom.getQuestions().size());

        return dto;
    }
}
