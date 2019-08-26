package com.bitcamp.project.project_4bit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    // 메일주소, 제목, 본문을 전달받아서 메일전송해주는 메서드
    public void sendEmail(String emailAddress, String msgSubject, String msgText) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(emailAddress);
        msg.setSubject(msgSubject);
        msg.setText(msgText);

        javaMailSender.send(msg);
    }
}
