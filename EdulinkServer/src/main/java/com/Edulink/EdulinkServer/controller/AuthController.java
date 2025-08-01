package com.Edulink.EdulinkServer.controller;


import com.Edulink.EdulinkServer.dao.UserRepository;
import com.Edulink.EdulinkServer.dto.user.UserRequestDTO;
import com.Edulink.EdulinkServer.dto.user.UserResponseDTO;
import com.Edulink.EdulinkServer.payload.ApiResponse;
import com.Edulink.EdulinkServer.service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/v1/auth")

public class AuthController {
    @Autowired
    private MyUserDetailService myUserDetailService;

    @Autowired
    private UserRepository userRepository;




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

}
