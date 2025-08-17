package com.Edulink.EdulinkServer.service;

import com.Edulink.EdulinkServer.model.Classroom;
import com.Edulink.EdulinkServer.model.embeddables.ClassMaterial;
import com.Edulink.EdulinkServer.model.embeddables.StudentInfo;
import com.Edulink.EdulinkServer.repository.ClassRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ClassroomService {


    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private Cloudinary cloudinary;



    public Classroom addClassroom(
            Classroom classroom,
            List<StudentInfo> students,

            List<MultipartFile> resourceFiles, List<String> resourceTitles, List<String> resourceDescriptions,
            List<MultipartFile> assignmentFiles, List<String> assignmentTitles, List<String> assignmentDescriptions,
            List<MultipartFile> taskFiles, List<String> taskTitles, List<String> taskDescriptions
    ) throws IOException {

        // Set students
        classroom.setStudents(students);

        // Upload resources, assignments, tasks
        classroom.setResources(uploadFiles(resourceFiles, resourceTitles, resourceDescriptions, "classroom_resources"));
        classroom.setAssignments(uploadFiles(assignmentFiles, assignmentTitles, assignmentDescriptions, "classroom_assignments"));
        classroom.setTasks(uploadFiles(taskFiles, taskTitles, taskDescriptions, "classroom_tasks"));

        // Save classroom
        return classRepository.save(classroom);
    }

    private List<ClassMaterial> uploadFiles(
            List<MultipartFile> files,
            List<String> titles,
            List<String> descriptions,
            String folder
    ) throws IOException {

        List<ClassMaterial> materials = new ArrayList<>();

        if (files != null) {
            for (int i = 0; i < files.size(); i++) {
                MultipartFile file = files.get(i);
                if (!file.isEmpty()) {
                    Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                            ObjectUtils.asMap("folder", folder));

                    ClassMaterial material = new ClassMaterial();
                    material.setTitle(titles.get(i));               // use title from client
                    material.setDescription(descriptions.get(i));   // use description from client
                    material.setFileUrl(uploadResult.get("secure_url").toString());
                    materials.add(material);
                }
            }
        }

        return materials;
    }



    // Util Function
    public Classroom findClassRoom (Long classroomId){
        return classRepository.findById(classroomId).orElseThrow(()->new RuntimeException("Class Room Not Found"));
    }

    public Classroom addResources(Long classroomId, List<MultipartFile> resourcesFiles , List<String> resourcesTitles , List<String> resourcesDescription ) throws IOException {

        Classroom classroom = findClassRoom(classroomId);

        // Get Existing Resources
        List<ClassMaterial> existingResources = classroom.getResources();

        List<ClassMaterial> newResources = uploadFiles(resourcesFiles, resourcesTitles, resourcesDescription, "classroom_resources");

        if(existingResources != null){
            existingResources.addAll(newResources);
        }else{
            existingResources  = newResources;
        }

        classroom.setResources(existingResources);

        return classRepository.save(classroom);
    }

    public Classroom addAssignments(Long classroomId, List<MultipartFile> assignmentFiles , List<String> assignmentTitle, List<String> assignmentDescription) throws IOException {

        Classroom classroom = findClassRoom(classroomId);

        // Get Existing Assignment
        List<ClassMaterial> existingAssignment = classroom.getAssignments();

        List<ClassMaterial> newAssignments = uploadFiles(assignmentFiles, assignmentTitle, assignmentDescription, "classroom_assignments");

        if(existingAssignment != null ){
            existingAssignment.addAll(newAssignments);
        }else {
            existingAssignment = newAssignments;
        }

        classroom.setAssignments(existingAssignment);

        return classRepository.save(classroom);
    }


    public Classroom addTask(Long classRoomId, List<MultipartFile> taskFiles, List<String> taskTitle, List<String> taskDescription) throws IOException {
        Classroom classroom = findClassRoom(classRoomId);

        // Get All Existing Task;

        List<ClassMaterial> existingTasks = classroom.getTasks();

        List<ClassMaterial> newClassTasks = uploadFiles(taskFiles, taskTitle,taskDescription, "classroom_tasks");

        if(existingTasks != null ){
            existingTasks.addAll(newClassTasks);
        } else {
            existingTasks = newClassTasks;
        }

        classroom.setTasks(existingTasks);

        return classRepository.save(classroom);
    }


}




