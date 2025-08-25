package com.Edulink.EdulinkServer.service;

import com.Edulink.EdulinkServer.model.StudentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;



    public void sendResetPasswordEmail(String toEmail , String resetLink){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("abdulbasitabdulwahab3@gmail.com");
        simpleMailMessage.setTo(toEmail);
        simpleMailMessage.setSubject("Password Reset Request");
        simpleMailMessage.setText("Click The Link to reset Your Password: " + resetLink);
        javaMailSender.send(simpleMailMessage);
    }

    public void sendStudentJoinNotification(String toEmail, String verifyLink, StudentInfo studentInfo){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("abdulbasitabdulwahab3@gmail.com");
        simpleMailMessage.setTo(toEmail);
        simpleMailMessage.setSubject("Classroom ApprovalRequest from " + studentInfo.getEmail());
        simpleMailMessage.setText("Click this Link to Approve this student " + verifyLink);
        javaMailSender.send(simpleMailMessage);
    }



//    String resetLink = "http://your-frontend.com/reset-password?token=" + token;
//emailService.sendPasswordResetEmail(user.getEmail(), resetLink);

}
