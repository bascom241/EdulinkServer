package com.Edulink.EdulinkServer.service;

import com.Edulink.EdulinkServer.dao.UserRepository;
import com.Edulink.EdulinkServer.dto.classroom.ClassroomResponseDto;
import com.Edulink.EdulinkServer.enums.QuestionType;
import com.Edulink.EdulinkServer.mapper.ClassroomMapper;
import com.Edulink.EdulinkServer.model.*;
import com.Edulink.EdulinkServer.model.embeddables.ClassMaterial;

import com.Edulink.EdulinkServer.repository.AnswerRepository;
import com.Edulink.EdulinkServer.repository.ClassRepository;
import com.Edulink.EdulinkServer.repository.QuestionRepository;
import com.Edulink.EdulinkServer.repository.StudentRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
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

    @Autowired
    private PayStackService payStackService;


    @Autowired
    private StudentRepository studentRepository;


    // Create a Classroom
    public Classroom addClassroom(
            Long ownerId,
            Classroom classroom,
            List<StudentInfo> students,

            List<MultipartFile> resourceFiles, List<String> resourceTitles, List<String> resourceDescriptions,
            List<MultipartFile> assignmentFiles, List<String> assignmentTitles, List<String> assignmentDescriptions,
            List<MultipartFile> taskFiles, List<String> taskTitles, List<String> taskDescriptions
    ) throws IOException {


        for(StudentInfo student : students){
            student.getClassrooms().add(classroom);
            classroom.getStudents().add(student);
        }






        // Upload resources, assignments, tasks
        classroom.setResources(uploadFiles(resourceFiles, resourceTitles, resourceDescriptions, "classroom_resources"));
        classroom.setAssignments(uploadFiles(assignmentFiles, assignmentTitles, assignmentDescriptions, "classroom_assignments"));
        classroom.setTasks(uploadFiles(taskFiles, taskTitles, taskDescriptions, "classroom_tasks"));

        User owner = userRepository.findById(ownerId).orElseThrow(() -> new RuntimeException("Classroom Owner Not Found"));


       classroom.setOwner(owner);


        if(owner.getBankCode() == null){
            String subAccountCode = payStackService.createSubAccount(
                    classroom.getOwner().getFirstName(),
                    classroom.getOwner().getBankCode(),
                    classroom.getOwner().getBankAccount(),
                    90
            );

            owner.setBankCode(subAccountCode);
            userRepository.save(classroom.getOwner());
        }


        Classroom savedClassroom = classRepository.save(classroom);

// Ensure students are updated
        studentRepository.saveAll(students);

        return savedClassroom;
    }

    // Utility Method to add files to Cloudinary

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



    // Util Method to Fin Classroom
    public Classroom findClassRoom (Long classroomId){
        return classRepository.findById(classroomId).orElseThrow(()->new RuntimeException("Class Room Not Found"));
    }

    public ClassroomResponseDto findClassInstructorRoom(Long classroomId) {
        Classroom classroom = classRepository.findById(classroomId)
                .orElseThrow(() -> new RuntimeException("Class Room Not Found"));

        // convert entity → DTO
        return ClassroomMapper.toDto(classroom);
    }



    // List classrooms that belongs to a single tutor

    public List<ClassroomResponseDto> findInstructorClassrooms(String email){
        User instructor = userRepository.findByEmail(email);
        if (instructor == null) {
            throw new RuntimeException("Instructor not found with email: " + email);
        }
        if(!instructor.isTeacher()){
            throw new RuntimeException("This is for instructor Only");
        }

        List<Classroom> classrooms = classRepository.findByOwner_Email(email);

        return classrooms.stream().map(ClassroomMapper::toDto).toList();
    }

    // fetch single Classroom
    public ClassroomResponseDto findSingleInstructorClassroom(String email , Long classId){
        User instructor = userRepository.findByEmail(email);
        if (instructor == null) {
            throw new RuntimeException("Instructor not found with email: " + email);
        }

        if (!instructor.isTeacher()) {
            throw new RuntimeException("This is for instructor Only");
        }

        return findClassInstructorRoom(classId);

    }


    // Util Method to Fin Classroom
    public Question findQuestion(Long questionId){
        return questionRepository.findById(questionId).orElseThrow(()->new RuntimeException("Question not Found"));
    }


    // Add Resources for students on classroom
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


    // Add Assignments for students in a classroom
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



    // Add File For Tasks for students
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


        if(question.getQuestionType() == null){
            throw new RuntimeException("Question Type is Required");
        }
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


    public boolean verifyJoinRequest(StudentInfo studentInfo,  Long classroomId, String teacherEmail){
        Classroom classroom = findClassRoom(classroomId);

        // Add student to list of students
        List<StudentInfo> studentInfoList = classroom.getStudents();

        boolean studentExist = studentInfoList.contains(studentInfo);
        if(studentExist){
            throw new RuntimeException("Student Already Exist in this classroom");
        }


        if(classroom.isClassroomFull()){
            throw new RuntimeException("Class room is Full Cant join");
        }
        String verifyStudent = "http://locahost:5173/notifications";
        emailService.sendStudentJoinNotification(teacherEmail, verifyStudent, studentInfo);

        return true;

    }


    // Fetching all Classrooms Created by instructors  (Todo)
    // Fetching all Classrooms Created by instructors and returning Length
    // Fetching all Classrooms Filterd by full or not ...


    public long getInstructorClassroomCount(String email){
        User instructor = userRepository.findByEmail(email);

        if(instructor == null){
            throw new RuntimeException("Instructor Not Found");
        }

        if(!instructor.isTeacher()){
            throw new RuntimeException("Onl Instructors can have classrooms");
        }

        return classRepository.countByOwner_Email(email);

    }

    public int getInstructorClassroomStudentCounts(String email){
        User instructor = userRepository.findByEmail(email);

        if(instructor == null){
            throw new RuntimeException("Instructor Not Found");
        }

        List<StudentInfo> students = studentRepository.findAll();
        List<Classroom> instructorClassrooms = classRepository.findByOwner_Email(email);

        List<StudentInfo> allStudents = instructorClassrooms.stream().flatMap(classroom -> classroom.getStudents().stream())
                .distinct()
                .toList();

        return allStudents.size();

    }













     // Not sure if it will be Implemented
    public Classroom enrollStudentToClassroom(StudentInfo studentInfo, Long classroomId){
        Classroom classroom = findClassRoom(classroomId);

        // Add student to list of students
        List<StudentInfo> studentInfoList = classroom.getStudents();

        classroom.setStudents(studentInfoList);

        return classRepository.save(classroom);

    }

}




