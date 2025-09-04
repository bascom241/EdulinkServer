package com.Edulink.EdulinkServer.service;

import com.Edulink.EdulinkServer.config.CloudinaryConfig;
import com.Edulink.EdulinkServer.dao.UserPrincipal;
import com.Edulink.EdulinkServer.dao.UserRepository;
import com.Edulink.EdulinkServer.dto.user.UserRequestDTO;
import com.Edulink.EdulinkServer.dto.user.UserResponseDTO;
import com.Edulink.EdulinkServer.model.User;
import com.cloudinary.utils.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CloudinaryConfig cloudinaryConfig;

    @Autowired
    private UserResponseDTO userResponseDTO;

    @Autowired
    private ModelMapper modelMapper;






    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if(user == null){
            throw new RuntimeException("User Not Found");
        }
        return new UserPrincipal(user);
    }


private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    public UserResponseDTO registerUser(UserRequestDTO userRequestDTO,
                                        MultipartFile certificate,
                                        MultipartFile governmentId) {
        try {
            User user = modelMapper.map(userRequestDTO, User.class);

            // Password validation
            String password = userRequestDTO.getPassword();
            String confirmPassword = userRequestDTO.getConfirmPassword();

            if (password == null || password.isBlank()) {
                throw new IllegalArgumentException("Password cannot be empty");
            }
            if (confirmPassword == null || confirmPassword.isBlank()) {
                throw new IllegalArgumentException("Confirm password cannot be empty");
            }
            if (!password.equals(confirmPassword)) {
                throw new IllegalArgumentException("Passwords do not match");
            }

            user.setPassword(bCryptPasswordEncoder.encode(password));
            user.setConfirmPassword(bCryptPasswordEncoder.encode(confirmPassword));

            // ✅ Upload only if teacher
            if (user.isTeacher()) {
                if (certificate == null || governmentId == null) {
                    throw new IllegalArgumentException("Teacher must provide certificate and government ID");
                }

                Map<String, Object> certificateUploadResult =
                        cloudinaryConfig.cloudinary().uploader().upload(certificate.getBytes(), ObjectUtils.emptyMap());

                Map<String, Object> governmentIdUploadResult =
                        cloudinaryConfig.cloudinary().uploader().upload(governmentId.getBytes(), ObjectUtils.emptyMap());

                String certificateUrl = (String) certificateUploadResult.get("secure_url");
                String governmentIdUrl = (String) governmentIdUploadResult.get("secure_url");

                user.setCertificateUrl(certificateUrl);
                user.setCertificateImageName(certificate.getOriginalFilename());
                user.setCertificateImageType(certificate.getContentType());

                user.setGovernmentIdUrl(governmentIdUrl);
                user.setGovernmentIdImageName(governmentId.getOriginalFilename());
                user.setGovernmentIdImageType(governmentId.getContentType());

                user.setRole("ROLE_TEACHER");
            } else {
                user.setRole("ROLE_STUDENT");
            }

            User savedUser = userRepository.save(user);

            // ✅ Build response
            UserResponseDTO response = new UserResponseDTO();
            response.setUserId(savedUser.getUserId());
            response.setFirstName(savedUser.getFirstName());
            response.setCertificateUrl(savedUser.getCertificateUrl());
            response.setGovernmentIdUrl(savedUser.getGovernmentIdUrl());

            return response;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}
