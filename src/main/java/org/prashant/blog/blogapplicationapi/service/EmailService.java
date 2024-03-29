package org.prashant.blog.blogapplicationapi.service;

import lombok.RequiredArgsConstructor;
import org.prashant.blog.blogapplicationapi.payload.MailBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    public void sendSimpleMessage(MailBody mailBody){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailBody.to());
        message.setFrom("ORG-MAIL");
        message.setSubject(mailBody.subject());
        message.setText(mailBody.text());
        javaMailSender.send(message);
    }
}
