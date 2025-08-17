package com.Edulink.EdulinkServer.controller;

import com.Edulink.EdulinkServer.model.Classroom;
import com.Edulink.EdulinkServer.model.embeddables.StudentInfo;

import com.Edulink.EdulinkServer.service.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ClassRoomController {


    @Autowired
private ClassroomService classroomService;

    @PostMapping("/create-class")
    public ResponseEntity<?> createClass (
            @RequestPart Classroom classroom,
            @RequestPart List<StudentInfo> students,

            @RequestPart(required = false) List<MultipartFile> resourcesFiles,
            @RequestPart(required = false) List<String> resourcesTitle,
            @RequestPart(required = false) List<String> resourcesDescription,

            @RequestPart(required = false) List<MultipartFile> assignmentFiles,
            @RequestPart(required = false) List<String> assignmentTitle,
            @RequestPart(required = false) List<String> assignmentDescription,

            @RequestPart(required = false) List<MultipartFile> taskFiles,
            @RequestPart(required = false) List<String> taskTitle,
            @RequestPart(required = false) List<String> taskDescription
            )throws IOException {

        try {
//            if ((resourcesFiles != null && (resourcesFiles.size() != resourcesTitle.size() || resourcesFiles.size() != resourcesDescription.size())) ||
//                    (assignmentFiles != null && (assignmentFiles.size() != assignmentTitle.size() || assignmentFiles.size() != assignmentDescription.size())) ||
//                    (taskFiles != null && (taskFiles.size() != taskTitle.size() || taskFiles.size() != taskDescription.size()))) {
//                throw new IllegalArgumentException("Files, titles, and descriptions lists must have the same size");
//            }

            Classroom savedClassRoom =classroomService.addClassroom (
                    classroom,
                    students,
                    resourcesFiles,    resourcesTitle,  resourcesDescription,
                    assignmentFiles, assignmentTitle, assignmentDescription,
                    taskFiles, taskTitle, taskDescription
            );
                return ResponseEntity.status(HttpStatus.CREATED).body(savedClassRoom);
        } catch (Exception e) {
                System.out.println(e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }



    }
}
