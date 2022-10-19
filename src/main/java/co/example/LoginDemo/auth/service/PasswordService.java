package co.example.LoginDemo.auth.service;

import co.example.LoginDemo.auth.entities.RequestPasswordDTO;
import co.example.LoginDemo.auth.entities.RestorePasswordDTO;
import co.example.LoginDemo.auth.repository.UserRepository;
import co.example.LoginDemo.notification.service.MailNotificationService;
import co.example.LoginDemo.user.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PasswordService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MailNotificationService mailNotificationService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void requestPassword (RequestPasswordDTO requestPasswordDTO) throws  Exception{
        User userFromDB = userRepository.findByEmail(requestPasswordDTO.getEmail());
        if (userFromDB == null) throw new Exception("User is null");
        userFromDB.setPasswordResetCode(UUID.randomUUID().toString());
        mailNotificationService.sendPasswordResetEmail(userFromDB);
        userRepository.save(userFromDB);

    }
    public User restorePassword(RestorePasswordDTO restorePasswordDTO) throws Exception{
        User userFromDB = userRepository.findByPasswordResetCode(restorePasswordDTO.getResetPasswordCode());
        if (userFromDB == null) throw new Exception("User is null");
        userFromDB.setPassword(passwordEncoder.encode(restorePasswordDTO.getNewPassword()));
        userFromDB.setPasswordResetCode(null);
        userRepository.save(userFromDB);

        //I am activating the user
        userFromDB.setActive(true);
        userFromDB.setActivationCode(null);
        return userRepository.save(userFromDB);

    }
}
