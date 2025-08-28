package com.Edulink.EdulinkServer.service;

import com.Edulink.EdulinkServer.model.StudentInfo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;


    private String defaultFrom;

    public void sendResetPasswordEmail(String toEmail, String resetLink) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setFrom("abdulbasitabdulwahab3@gmail.com");
            helper.setTo(toEmail);
            helper.setSubject("Password Reset Request");

            String html = EmailTemplates.resetPasswordHtml(resetLink);
            // optional plain-text fallback:
            String text = "Click the link to reset your password: " + resetLink;

            helper.setText(text, html); // (plain, html)
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send reset password email", e);
        }
    }

    public void sendStudentJoinNotification(String toEmail, String verifyLink, StudentInfo studentInfo) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8"); // enable multipart

            helper.setFrom("abdulbasitabdulwahab3@gmail.com");
            helper.setTo(toEmail);
            helper.setSubject("Classroom Approval Request from " + studentInfo.getEmail());

            String html = EmailTemplates.joinApprovalHtml(verifyLink, studentInfo);
            String text = "Approve this student: " + verifyLink;

            helper.setText(text, html); // plain + html
            javaMailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send student join notification", e);
        }
    }

}
