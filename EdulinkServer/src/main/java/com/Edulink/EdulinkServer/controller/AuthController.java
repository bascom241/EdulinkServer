package com.Edulink.EdulinkServer.controller;


import com.Edulink.EdulinkServer.dao.UserRepository;
import com.Edulink.EdulinkServer.dto.user.UserRequestDTO;
import com.Edulink.EdulinkServer.dto.user.UserResponseDTO;
import com.Edulink.EdulinkServer.model.User;
import com.Edulink.EdulinkServer.payload.ApiResponse;
import com.Edulink.EdulinkServer.service.JwtService;
import com.Edulink.EdulinkServer.service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin("*")
public class AuthController {
    @Autowired
    private MyUserDetailService myUserDetailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    private JwtService jwtService;


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestPart UserRequestDTO userRequestDTO, @RequestPart MultipartFile certificate , @RequestPart MultipartFile governmentId){
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

}
