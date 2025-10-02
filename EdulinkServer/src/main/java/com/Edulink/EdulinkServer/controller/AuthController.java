package com.Edulink.EdulinkServer.controller;


import com.Edulink.EdulinkServer.dao.UserRepository;
import com.Edulink.EdulinkServer.dto.user.UserRequestDTO;
import com.Edulink.EdulinkServer.dto.user.UserResponseDTO;
import com.Edulink.EdulinkServer.mapper.UserMapper;
import com.Edulink.EdulinkServer.model.User;
import com.Edulink.EdulinkServer.payload.ApiResponse;
import com.Edulink.EdulinkServer.service.BlacklistedTokenService;
import com.Edulink.EdulinkServer.service.EmailService;
import com.Edulink.EdulinkServer.service.JwtService;
import com.Edulink.EdulinkServer.service.MyUserDetailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin("http://localhost:5173")
public class AuthController {
    @Autowired
    private MyUserDetailService myUserDetailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    private JwtService jwtService;

    @Autowired
    private EmailService emailService;


    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BlacklistedTokenService blacklistedTokenService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestPart UserRequestDTO userRequestDTO, @RequestPart(value = "certificate" , required = false) MultipartFile certificate , @RequestPart(value = "governmentId" , required = false) MultipartFile governmentId){
        try {
            if(userRequestDTO.getEmail() != null && userRepository.findByEmail(userRequestDTO.getEmail()) != null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User Already Exits");
            }
            UserResponseDTO user = myUserDetailService.registerUser(userRequestDTO, certificate, governmentId);
            ApiResponse<UserResponseDTO> response = new ApiResponse<>("User Created", user);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser (@RequestBody UserRequestDTO user){

        System.out.println("EMAIL: " + user.getEmail());
        System.out.println("RAW PASSWORD: " + user.getPassword());

        try {
            // Fix this condition
            if (user.getEmail() == null || userRepository.findByEmail(user.getEmail()) == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not Found");
            }

            // Fetch the user and print password (for debugging)
            User foundUser = userRepository.findByEmail(user.getEmail());

            if (foundUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not Found");
            }

            System.out.println("STORED PASSWORD: " + foundUser.getPassword());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
            );

            if(authentication.isAuthenticated()){
                return ResponseEntity.status(HttpStatus.OK).body(jwtService.generateToken(user.getEmail()));
            }else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UnAuthorized Request Sent");
            }

        } catch (Exception e) {
            e.printStackTrace(); // Show full exception
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @GetMapping("/hello")
    public String greetUser(){
        return "Greeting here";
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody UserRequestDTO request){
        System.out.println(request);
        try {
            if (request.getEmail() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is required");
            }

            User user = userRepository.findByEmail(request.getEmail());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not Found");
            }

            // generate token
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setTokenExpiry(LocalDateTime.now().plusMinutes(15));

            // save entity
            userRepository.save(user);



            // send reset link
            String resetLink = "http://localhost:5173/reset-password?token=" + token;
            emailService.sendResetPasswordEmail(user.getEmail(), resetLink);

            return ResponseEntity.status(HttpStatus.OK).body("Reset Password Sent Successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token , @RequestBody UserRequestDTO requestDTO){
        try {
            User user = userRepository.findByToken(token);
            if(user == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token Not Found");
            }
            if(user.getTokenExpiry().isBefore(LocalDateTime.now())){
                throw new RuntimeException("Token Expired");
            }



            user.setPassword(bCryptPasswordEncoder.encode(requestDTO.getPassword()));
            user.setConfirmPassword(bCryptPasswordEncoder.encode(requestDTO.getConfirmPassword()));

            user.setToken(null);
            user.setTokenExpiry(null);

            userRepository.save(user);

            return ResponseEntity.status(HttpStatus.OK).body("Password Reset Successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @PostMapping("/user")
    public ResponseEntity<?> getUser (Authentication authentication){
        System.out.println("Request Hit ");
        try {

            User user = userRepository.findByEmail(authentication.getName());
            System.out.println(user);
            if(user == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
            }

            // Todo Map the Dto together
            UserResponseDTO userResponseDTO = modelMapper.map(user, UserResponseDTO.class);
            return ResponseEntity.status(HttpStatus.OK).body(userResponseDTO);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // Logout

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader ("Authorization") String authHeader){
        try {
            if(authHeader != null && authHeader.startsWith("Bearer ")){
                String token = authHeader.substring(7);

                LocalDateTime expiry = jwtService.extractExpiration(token).toInstant()
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDateTime();

                blacklistedTokenService.blacklistToken(token, expiry);

            }
            return ResponseEntity.status(HttpStatus.OK).body("Logged Out Successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

}
