package co.example.LoginDemo.notification.service;

import co.example.LoginDemo.user.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class MailNotificationService {
    @Autowired
    private JavaMailSender emailSender;
    public void sendActivationEmail(User user) {
        SimpleMailMessage sms = new SimpleMailMessage();
        sms.setTo(user.getEmail());
        sms.setFrom("linda@gmail.co");
        sms.setReplyTo("---");
        sms.setSubject("---");
        sms.setText("..." + user.getActivationCode());
        emailSender.send(sms);
    }

    public void sendPasswordResetEmail(User user) {
        SimpleMailMessage sms = new SimpleMailMessage();
        sms.setTo(user.getEmail());
        sms.setFrom("linda@gmail.co");
        sms.setReplyTo("---");
        sms.setSubject("---");
        sms.setText("..." + user.getActivationCode());
        emailSender.send(sms);
    }

}
