package com.Edulink.EdulinkServer.mapper;

import com.Edulink.EdulinkServer.dto.classroom.ClassroomResponseDto;
import com.Edulink.EdulinkServer.model.Classroom;

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

        dto.setResources(classroom.getResources());
        dto.setAssignments(classroom.getAssignments());
        dto.setTasks(classroom.getTasks());

        dto.setNumberOfStudents(classroom.getStudents().size());
        dto.setNumberOfSessions(classroom.getSessions().size());
        dto.setNumberOfQuestions(classroom.getQuestions().size());

        return dto;
    }
}
