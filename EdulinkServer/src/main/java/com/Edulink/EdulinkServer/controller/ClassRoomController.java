package com.Edulink.EdulinkServer.controller;

import com.Edulink.EdulinkServer.dao.UserRepository;
import com.Edulink.EdulinkServer.dto.StudentInfoDto;
import com.Edulink.EdulinkServer.dto.classroom.ClassroomResponseDto;
import com.Edulink.EdulinkServer.dto.notification.NotificationDTO;
import com.Edulink.EdulinkServer.dto.notification.NotificationMessage;
import com.Edulink.EdulinkServer.model.*;

import com.Edulink.EdulinkServer.repository.*;
import com.Edulink.EdulinkServer.service.ClassroomService;
import com.Edulink.EdulinkServer.service.InviteLinkService;
import com.Edulink.EdulinkServer.service.PayStackService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.management.Query;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/v1/classroom")
public class ClassRoomController {

@Autowired
private InviteLinkService inviteLinkService;



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

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private WalletRepository walletRepository;


    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping(value = "/create-class/{classroomOwner}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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
            @RequestPart(required = false) List<String> taskDescription,

            HttpServletRequest request
            )throws IOException {
        System.out.println("Content-Type: " + request.getContentType());
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

    @GetMapping("/instructor-classrooms/{email}")
    public ResponseEntity<?> getAllClassrooms (@PathVariable(name = "email") String email){
        try {
            List<ClassroomResponseDto> classrooms = classroomService.findInstructorClassrooms(email);
            if(classrooms.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Class list is Empty, Create classroom to see your class lists");
            }

            return ResponseEntity.status(HttpStatus.OK).body(classrooms);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/single-instructor-classroom/{classId}")
    public ResponseEntity<?> getSingleInstructorClassroom(@RequestParam ("email") String email , @PathVariable(name = "classId") Long classId){
        try {
            ClassroomResponseDto classroom = classroomService.findSingleInstructorClassroom(email, classId);
            return ResponseEntity.status(HttpStatus.OK).body(classroom);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // For Students To view Study Resources

    @PutMapping(value = "/add-resources/{classroomId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addResources(
            @PathVariable(name = "classroomId") Long classroomId,
            @RequestPart(required = false) List<MultipartFile> resourcesFiles,
            @RequestParam(required = false , name = "resourcesTitle[]") List<String> resourcesTitle,
            @RequestParam(required = false, name = "resourcesDescription[]") List<String> resourcesDescription) {

        System.out.print("Server Hit");
        try {
            System.out.println("Classroom ID: " + classroomId);
            System.out.println("Titles: " + resourcesTitle);
            System.out.println("Descriptions: " + resourcesDescription);
            System.out.println("Files: " + (resourcesFiles != null ? resourcesFiles.size() : 0));

            Classroom foundClassRoom = classroomService.addResources(classroomId, resourcesFiles, resourcesTitle, resourcesDescription);
            if (foundClassRoom == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Class Room Not Found");
            }
            return ResponseEntity.status(HttpStatus.OK).body(foundClassRoom.getResources()); // Return just the resources
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    // Get all the students in a teacher classroom
    @GetMapping("/my-students")
    public ResponseEntity<?> getAllInstructorClassrooms (@RequestParam String teacherEmail){
        System.out.println("Server Hit");
        try {
            List<StudentInfoDto> allStudents = classroomService.getMyClassroomStudents(teacherEmail);

            return ResponseEntity.status(HttpStatus.OK).body(allStudents);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    // Upload Recorded Lectures  // for sessions
    @PutMapping(value = "/add-assignment/{classroomId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addAssignment (
            @PathVariable(name = "classroomId")Long classRoomId,
            @RequestParam(required = false) List<MultipartFile> assignmentFiles,
            @RequestParam(required = false) List<String> assignmentTitle,
            @RequestParam(required = false) List<String> assignmentDescription ){
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




    // Upload Schedule Resources for students
    @PutMapping(value = "/add-task/{classroomId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addTask(
            @PathVariable(name = "classroomId")Long classRoomId ,
            @RequestPart(required = false) List<MultipartFile> taskFiles,
            @RequestParam(required = false, name = "taskTitle[]") List<String> taskTitle,
            @RequestParam(required = false, name = "taskDescription[]") List<String> taskDescription){
        try {

            System.out.println("Class Id: " +  classRoomId);
            System.out.println(" taskFiles: " + (taskFiles != null ? taskFiles.size(): 0));
            System.out.println("task Title: " + taskTitle);
            System.out.println("taskDescription: " + taskDescription);
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

            User student = userRepository.findByEmail(studentInfo.getEmail());
            if (student == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("You can only use registered email to join classrooms");
            }


            boolean isStudentExists = classroom.getStudents().stream()
                    .anyMatch(s -> s.getStudentId().equals(student.getUserId()));
            if (isStudentExists) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Already Enrolled in this classroom");
            }

            boolean eligible = classroomService.verifyJoinRequest(studentInfo, classroomId, teacherEmail);
            if (!eligible) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Not Eligible to join this classroom");
            }
            if(amount < classroom.getClassroomPrice()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please set the correct Price");
            }

            // Some Data which will be displayed in the transaction
            Map< String , Object > metadata = new HashMap<>();
            Map<String , String > response = payStackService.initializePayment(studentInfo, classroom, ownerId, amount, metadata);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @GetMapping("/{id}/invite")
    public Map<String, String> getInviteLink(@PathVariable Long id) {
        Classroom classroom = classRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Classroom not found"));

        if (classroom.isClassroomFull()) {
            throw new RuntimeException("Classroom full ");
        }

        String link = inviteLinkService.generateInviteLink(classroom.getClassId());

        classroom.setInviteLink(link);
        classRepository.save(classroom);

        return Map.of("inviteLink", link);
    }

    @GetMapping("/class-count")
    public ResponseEntity<?> getInstructorClassroomCount(@RequestParam("email") String email){
        try {
            long classRoomLength = classroomService.getInstructorClassroomCount(email);
            return ResponseEntity.ok(classRoomLength);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }



    @Transactional
    @PostMapping("/webhook")
    public ResponseEntity<?> webHook(@RequestBody Map<String, Object> payload) {
        try {
            System.out.println("== Incoming Webhook ==");
            System.out.println("Payload: " + new ObjectMapper().writeValueAsString(payload));

            Object eventObj = payload.get("event");
            System.out.println("Event object: " + eventObj + " (type: " + (eventObj != null ? eventObj.getClass().getName() : "null") + ")");

            if ("charge.success".equalsIgnoreCase(String.valueOf(eventObj).trim())) {
                System.out.println("Charged was a success ✅");

                Map<String, Object> data = (Map<String, Object>) payload.get("data");
                String reference = (String) data.get("reference");
                BigDecimal amount = new BigDecimal(data.get("amount").toString()); // amount in kobo
                String status = (String) data.get("status");
                String currency = (String) data.get("currency");


                System.out.println("Ref: " + reference + ", Amount: " + amount + ", Status: " + status);

                Map<String, Object> subaccount = (Map<String, Object>) data.get("subaccount");
                String subaccountCode = subaccount != null ? (String) subaccount.get("subaccount_code") : null;

                // metadata
                Map<String, Object> metadata = (Map<String, Object>) data.get("metadata");
                Long classroomOwnerId = Long.valueOf(metadata.get("ownerId").toString());
                String classroomName = (String) metadata.get("classroom");
                String classroomDescription = (String) metadata.get("classroomDescription");
                BigDecimal classroomPrice = new BigDecimal(metadata.get("classroomPrice").toString());
                Long classroomId = Long.valueOf(metadata.get("classroomId").toString());

                Classroom classroom = classRepository.findById(classroomId).orElseThrow(()-> new RuntimeException("Classroom Not Found"));
                // customer info
                Map<String, Object> customer = (Map<String, Object>) data.get("customer");
                String email = (String) customer.get("email");
                System.out.println(email);
                User student = userRepository.findByEmail(email);
                User classroomOwner = userRepository.findById(classroomOwnerId).orElse(null);

                Optional<StudentInfo> studentInfoOpt = studentRepository.findFirstByEmail(student.getEmail());
                System.out.println("Looking for studentInfo with email: " + student.getEmail());
                System.out.println("Found studentInfo? " + studentInfoOpt.isPresent());

                if (studentInfoOpt.isPresent()) {
                    StudentInfo studentInfo = studentInfoOpt.get();
                    System.out.println("StudentInfo loaded: " + studentInfo.getFirstName() + " " + studentInfo.getLastName());

                    if (!studentInfo.getClassrooms().contains(classroom)) {
                        System.out.println("Student not yet in classroom " + classroom.getClassName() + ". Adding now...");

                        // ✅ Update owning side
                        studentInfo.getClassrooms().add(classroom);

                        // optional (sync in memory)
                        classroom.getStudents().add(studentInfo);

                        studentRepository.save(studentInfo); // only owning side must be saved
                        System.out.println("Student added to classroom and saved ✅");
                    } else {
                        System.out.println("Student already in classroom " + classroom.getClassName());
                    }

                } else {
                    System.out.println("No StudentInfo found. Creating new StudentInfo for " + student.getEmail());

                    StudentInfo newStudentInfo = new StudentInfo();
                    newStudentInfo.setEmail(student.getEmail());
                    newStudentInfo.setFirstName(student.getFirstName());
                    newStudentInfo.setLastName(student.getLastName());
                    newStudentInfo.setUser(student);

                    // ✅ Add classroom (owning side)
                    newStudentInfo.getClassrooms().add(classroom);

                    studentRepository.save(newStudentInfo);
                    System.out.println("New studentInfo created and saved with classroom ✅");
                }

// Debug check: list all students in this classroom
                Classroom debugClassroom = classRepository.findById(classroom.getClassId()).orElseThrow();
                System.out.println("Current students in classroom " + debugClassroom.getClassName() + ":");
                debugClassroom.getStudents().forEach(s ->
                        System.out.println(" -> " + s.getEmail() + " (" + s.getFirstName() + " " + s.getLastName() + ")")
                );


                // check if order already exists
                if (orderRepository.findByReference(reference) == null) {
                    // === Calculate fees ===
                    BigDecimal platformFee = amount.multiply(BigDecimal.valueOf(0.10));
                    BigDecimal teacherAmount = amount.subtract(platformFee);

                    // === Save Order ===
                    Order order = new Order();
                    order.setReference(reference);
                    order.setAmount(amount);
                    order.setSettlementAmount(teacherAmount);
                    order.setCurrency(currency);
                    order.setStatus(status);
                    order.setStudent(student);
                    order.setClassroomOwner(classroomOwner);
                    order.setSubaccountCode(subaccountCode);
                    order.setCreatedAt(new Date());

                    order.setClassroomName(classroomName);
                    order.setClassroomDescription(classroomDescription);
                    order.setClassroomPrice(classroomPrice);
                    orderRepository.save(order);
                    System.out.println("Order saved ✅");

                    // === Update Wallet ===
                    Wallet wallet = walletRepository.findByWalletOwner_UserId(classroomOwnerId)
                            .orElseGet(() -> {
                                Wallet newWallet = new Wallet();
                                newWallet.setWalletOwner(classroomOwner);
                                return walletRepository.save(newWallet);
                            });

                    wallet.setBalance(wallet.getBalance().add(teacherAmount));
                    wallet.setTotalEarnings(wallet.getTotalEarnings().add(teacherAmount));
                    walletRepository.save(wallet);


                    Notification notification = new Notification();
                    notification.setClassroom(classroom);
                    notification.setContent(student.getFirstName() + "has Joined your classroom " + classroom.getClassName() );
                    notification.setTimestamp(LocalDateTime.now());
                    notification.setType("STUDENT JOINED");
                    notification.setTeacher(classroomOwner);

                    notificationRepository.save(notification);


                    System.out.println("Saved to database");
                    assert classroomOwner != null;
                    String destination = "/topic/classroom." + classroomOwner.getUserId();

                    NotificationDTO notificationDTO = new NotificationDTO(notification.getId(),
                            notification.getTimestamp(), notification.getContent(), notification.getType(),
                            notification.getTeacher().getUserId(), notification.getClassroom().getClassName(), notification.getRead()

                    );
                    simpMessagingTemplate.convertAndSend(destination, notificationDTO);
                    System.out.println(destination);
                    System.out.println(simpMessagingTemplate);
                    System.out.println("Wallet updated ✅");
                }
            } else {
                System.out.println("Event was not charge.success: " + eventObj);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing webhook: " + e.getMessage());
        }

        return ResponseEntity.ok("Webhook received");
    }


}

