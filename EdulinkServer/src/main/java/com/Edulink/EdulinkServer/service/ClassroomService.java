package com.Edulink.EdulinkServer.service;

import com.Edulink.EdulinkServer.dao.UserRepository;
import com.Edulink.EdulinkServer.enums.QuestionType;
import com.Edulink.EdulinkServer.model.*;
import com.Edulink.EdulinkServer.model.embeddables.ClassMaterial;

import com.Edulink.EdulinkServer.repository.AnswerRepository;
import com.Edulink.EdulinkServer.repository.ClassRepository;
import com.Edulink.EdulinkServer.repository.QuestionRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class ClassroomService {


    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;



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

    public Question findQuestion(Long questionId){
        return questionRepository.findById(questionId).orElseThrow(()->new RuntimeException("Question not Found"));
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


    public Classroom addQuestions(Long classroomId, Question question){
        Classroom classroom = findClassRoom(classroomId);
        question.setClassroom(classroom);
        classroom.getQuestions().add(question);
        return classRepository.save(classroom);
    }


    public StudentAnswer submitAnswer(Long questionId, StudentInfo studentInfo, String answerText){
        Question question = findQuestion(questionId);


        if (question.getQuestionType() == QuestionType.NUMBER) {
            try {
                Integer.parseInt(answerText);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Answer must be a number!");
            }
        }

        if(question.getQuestionType() == QuestionType.MULTIPLE_CHOICE){
            List<String> validOptions = Arrays.asList(question.getMultipleChoice().split(","));
            if (!validOptions.contains(answerText)) {
                throw new RuntimeException("Answer must be one of: " + validOptions);
            }
        }

        StudentAnswer answer = new StudentAnswer();
        answer.setQuestion(question);
        answer.setStudentInfo(studentInfo);
        answer.setAnswer(answerText);

        return answerRepository.save(answer);


    }




//    public void joincClassroom(StudentInfo studentInfo, int classPrice , Long classroomId, String teacherEmail){
//        Classroom classroom = findClassRoom(classroomId);
//
//        // Add student to list of students
//        List<StudentInfo> studentInfoList = classroom.getStudents();
//
//        boolean studentExist = studentInfoList.contains(studentInfo);
//        if(studentExist){
//            throw new RuntimeException("Student Already Exist in this classroom");
//        } else {
//            studentInfoList.add(studentInfo);
//        }
//        classroom.setStudents(studentInfoList);
//
//        // send Email to the teacher for verification
//        String verifyLink = "http://locahost:5173/studentInfo";
//        emailService.sendVerifyStudentInformationRequest(teacherEmail, verifyLink, studentInfo );
//
//         // Show the teacher the list of students
//
//
//    }




    public boolean verifyJoinRequest(StudentInfo studentInfo,  Long classroomId, String teacherEmail){
        Classroom classroom = findClassRoom(classroomId);

        // Add student to list of students
        List<StudentInfo> studentInfoList = classroom.getStudents();

        boolean studentExist = studentInfoList.contains(studentInfo);
        if(studentExist){
            throw new RuntimeException("Student Already Exist in this classroom");
        }
//
//        String email = authentication.getName();
//        User user = userRepository.findByEmail(email);
//        if (user == null){
//            throw new RuntimeException("User Does not exits ");
//        }
        if(!classroom.isClassroomFull()){
            throw new RuntimeException("Class room is Full Cant join");
        }
        String verifyStudent = "http://locahost:5173/notifications";
        emailService.sendStudentJoinNotification(teacherEmail, verifyStudent, studentInfo);

        return true;

    }

    public Classroom enrollStudentToClassroom(StudentInfo studentInfo, Long classroomId){
        Classroom classroom = findClassRoom(classroomId);

        // Add student to list of students
        List<StudentInfo> studentInfoList = classroom.getStudents();

        classroom.setStudents(studentInfoList);

        return classRepository.save(classroom);

    }






}




