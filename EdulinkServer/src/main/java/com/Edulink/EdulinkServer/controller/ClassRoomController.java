package com.Edulink.EdulinkServer.controller;

import com.Edulink.EdulinkServer.model.Classroom;
import com.Edulink.EdulinkServer.model.embeddables.StudentInfo;

import com.Edulink.EdulinkServer.repository.ClassRepository;
import com.Edulink.EdulinkServer.service.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ClassRoomController {


    @Autowired
private ClassroomService classroomService;

    @Autowired
    private ClassRepository classRepository;

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

    @PutMapping("/add-resources/{classroomId}")
    public ResponseEntity<?> addResources (@PathVariable(name = "classroomId") Long classroomId, @RequestPart(required = false) List<MultipartFile> resourcesFiles,@RequestPart(required = false) List<String> resourcesTitle, @RequestPart(required = false) List<String> resourcesDescription){
        try {
            System.out.println(classroomId);
            System.out.println(resourcesDescription);
            System.out.println(resourcesFiles);
            Classroom foundClassRoom = classroomService.addResources(classroomId,resourcesFiles,resourcesTitle,resourcesDescription);
            if(foundClassRoom == null ){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Class Room Not Found");
            }
            return ResponseEntity.status(HttpStatus.OK).body(foundClassRoom);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/add-assignment/{classroomId}")
    public ResponseEntity<?> addAssignment (@PathVariable(name = "classroomId")Long classRoomId,     @RequestPart(required = false) List<MultipartFile> assignmentFiles, @RequestPart(required = false) List<String> assignmentTitle, @RequestPart(required = false) List<String> assignmentDescription ){
        try {
            Classroom foundClassroom = classroomService.addAssignments(classRoomId,assignmentFiles,assignmentTitle,assignmentDescription);
            if(foundClassroom == null ){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Class Room Not Found");
            }

            return ResponseEntity.status(HttpStatus.OK).body(foundClassroom);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/add-task/{classroomId}")
    public ResponseEntity<?> addTask(@PathVariable(name = "classroomId")Long classRoomId ,  @RequestPart(required = false) List<MultipartFile> taskFiles, @RequestPart(required = false) List<String> taskTitle, @RequestPart(required = false) List<String> taskDescription){
        try {
            Classroom foundClassRoom = classroomService.addTask(classRoomId,taskFiles,taskTitle, taskDescription);
            if(foundClassRoom == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Class Room Not Found");
            }
            return ResponseEntity.status(HttpStatus.OK).body(foundClassRoom);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}

