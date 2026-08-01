package com.saksham.Ecommerce.service;

import com.saksham.Ecommerce.entity.Seller;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendVerificationOtpEmail(String userEmail, String otp, String subject, String text) throws MessagingException {

        try {
            System.out.println("inside Send Verification Otp Email Class");
            System.out.println("Sending verification OTP email to " + userEmail);
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setSubject(subject);
            helper.setText(text);
            helper.setTo(userEmail);
//            helper.setFrom("its.sakshamsrivastava@gmail.com");
            mailSender.send(mimeMessage);
        } catch (MailException e) {
            throw new MailSendException("fail to send email");
        }
    }
}
