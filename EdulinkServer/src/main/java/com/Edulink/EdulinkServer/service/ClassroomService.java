package com.Edulink.EdulinkServer.service;

import com.Edulink.EdulinkServer.model.Classroom;
import com.Edulink.EdulinkServer.model.embeddables.ClassMaterial;
import com.Edulink.EdulinkServer.model.embeddables.StudentInfo;
import com.Edulink.EdulinkServer.repository.ClassRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
}
