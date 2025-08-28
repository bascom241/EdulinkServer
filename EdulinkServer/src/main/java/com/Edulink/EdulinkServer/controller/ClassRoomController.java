package com.Edulink.EdulinkServer.controller;

import com.Edulink.EdulinkServer.dao.UserRepository;
import com.Edulink.EdulinkServer.model.*;

import com.Edulink.EdulinkServer.repository.ClassRepository;
import com.Edulink.EdulinkServer.repository.OrderRepository;
import com.Edulink.EdulinkServer.service.ClassroomService;
import com.Edulink.EdulinkServer.service.PayStackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.management.Query;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ClassRoomController {


    @Autowired
private ClassroomService classroomService;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private PayStackService payStackService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/create-class/{classroomOwner}")
    public ResponseEntity<?> createClass (
            @PathVariable(name = "classroomOwner") Long classroomOwner,
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


            Classroom savedClassRoom =classroomService.addClassroom (
                    classroomOwner,
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

    // Set question
    @PostMapping("/set-question/{classroomId}")
    public ResponseEntity<?> addQuestion(@PathVariable(name ="classroomId") Long classroomId, @RequestBody Question question){
        try {
            Classroom classroom = classroomService.addQuestions(classroomId, question);
            return ResponseEntity.status(HttpStatus.OK).body(classroom);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // Answer Question request
    @PostMapping("/answer-question/{questionId}")
    public ResponseEntity<?> submitAnswer(@PathVariable(name = "questionId") Long questionId, @RequestParam Long studentId , @RequestParam String answer){
        try {
            StudentInfo studentInfo = new StudentInfo();
            studentInfo.setStudentId(studentId);

            StudentAnswer studentAnswer = classroomService.submitAnswer(questionId,studentInfo,answer);
            return ResponseEntity.status(HttpStatus.OK).body(studentAnswer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/join/{classroomId}/{ownerId}")
    public ResponseEntity<?> joinClassroom(@PathVariable(name = "classroomId") Long classroomId, @PathVariable(name = "ownerId") Long ownerId, @RequestBody  StudentInfo studentInfo, @RequestParam String teacherEmail, @RequestParam int amount){
        try {
            Classroom classroom = classroomService.findClassRoom(classroomId);

            boolean isStudentEligibleToJoinClass = classroomService.verifyJoinRequest( studentInfo, classroomId, teacherEmail);

            if(!isStudentEligibleToJoinClass){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not Eligible");
            }
            if(amount < classroom.getClassroomPrice()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please set the correct Price");
            }
            Map<String , String > response = payStackService.initializePayment(studentInfo, ownerId, amount);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<?> webHook(@RequestBody Map<String, Object> payload) {
        try {
            String event = (String) payload.get("event");

            if ("charge.success".equals(event)) {
                Map<String, Object> data = (Map<String, Object>) payload.get("data");
                String reference = (String) data.get("reference");
                int amount = (int) data.get("amount"); // amount in kobo
                String status = (String) data.get("status");
                String currency = (String) data.get("currency");

                Map<String, Object> subaccount = (Map<String, Object>) data.get("subaccount");
                String subaccountCode = subaccount != null ? (String) subaccount.get("subaccount_code") : null;

                // metadata
                Map<String, Object> metadata = (Map<String, Object>) data.get("metadata");
                Map<String, Object> classroomIdMap = (Map<String, Object>) metadata.get("classroomId");
                Long classroomOwnerId = Long.valueOf(classroomIdMap.get("userId").toString());

                // customer info
                Map<String, Object> customer = (Map<String, Object>) data.get("customer");
                String email = (String) customer.get("email");
                User student = userRepository.findByEmail(email);

                User classroomOwner = userRepository.findById(classroomOwnerId).orElse(null);

                // check if order already exists
                if (orderRepository.findByReference(reference) == null) {
                    Order order = new Order();
                    order.setReference(reference);
                    order.setAmount(amount);

                    int platformFee = (int) (amount * 0.10);   // 10% platform fee
                    int teacherAmount = amount - platformFee;

                    order.setSettlementAmount(teacherAmount); // 90% goes to teacher
                    order.setCurrency(currency);
                    order.setStatus(status);
                    order.setStudent(student);
                    order.setClassroomOwner(classroomOwner);
                    order.setSubaccountCode(subaccountCode);
                    order.setCreatedAt(new Date());

                    orderRepository.save(order);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing webhook: " + e.getMessage());
        }

        return ResponseEntity.ok("Webhook received");
    }

}

