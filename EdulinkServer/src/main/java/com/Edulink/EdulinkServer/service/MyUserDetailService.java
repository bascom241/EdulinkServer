package com.Edulink.EdulinkServer.service;

import com.Edulink.EdulinkServer.config.CloudinaryConfig;
import com.Edulink.EdulinkServer.dao.UserPrincipal;
import com.Edulink.EdulinkServer.dao.UserRepository;
import com.Edulink.EdulinkServer.dto.user.UserRequestDTO;
import com.Edulink.EdulinkServer.dto.user.UserResponseDTO;
import com.Edulink.EdulinkServer.model.User;
import com.cloudinary.utils.ObjectUtils;
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




    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if(user == null){
            throw new RuntimeException("User Not Found");
        }
        return new UserPrincipal(user);
    }


private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);


public UserResponseDTO registerUser(UserRequestDTO userRequestDTO, MultipartFile certificate, MultipartFile governmentId){
    try {
        Map <String ,Object> certificateUploadResult = cloudinaryConfig.cloudinary().uploader().upload(certificate.getBytes(), ObjectUtils.emptyMap());
        Map<String, Object> governmentIdUploadResult = cloudinaryConfig.cloudinary().uploader().upload(governmentId.getBytes(), ObjectUtils.emptyMap());
        System.out.println(governmentIdUploadResult + "" +  certificateUploadResult);

        String certificateUrl = (String) certificateUploadResult.get("secure_url");
        String governmentIdUrl = (String) governmentIdUploadResult.get("secure_url");

        User user = new User();

        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userRequestDTO.getPassword()));
        user.setConfirmPassword(bCryptPasswordEncoder.encode(userRequestDTO.getConfirmPassword()));

        user.setCertificateUrl(certificateUrl);
        user.setCertificateImageName(certificate.getOriginalFilename());
        user.setCertificateImageType(certificate.getContentType());

        user.setGovernmentIdUrl(governmentIdUrl);
        user.setGovernmentIdImageName(governmentId.getOriginalFilename());
        user.setCertificateImageType(governmentId.getContentType());
        user.setRole("ROLE_USER");


        User savedUser = userRepository.save(user);

        userResponseDTO.setUserId(savedUser.getUserId());
        userResponseDTO.setFirstName(savedUser.getFirstName());
        userResponseDTO.setCertificateUrl(savedUser.getCertificateUrl());
        userResponseDTO.setGovernmentIdUrl(savedUser.getGovernmentIdUrl());

        return userResponseDTO;
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}



}
