package co.example.LoginDemo.auth.service;

import co.example.LoginDemo.auth.entities.SignupActivationDTO;
import co.example.LoginDemo.auth.entities.SignupDTO;
import co.example.LoginDemo.auth.repository.UserRepository;
import co.example.LoginDemo.notification.service.MailNotificationService;
import co.example.LoginDemo.user.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SignupService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MailNotificationService mailNotificationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User signup (SignupDTO signupDTO){
        User user = new User();
        user.setName(signupDTO.getName());
        user.setSurname(signupDTO.getSurname());
        user.setEmail(signupDTO.getEmail());
        user.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
        // cancello perchèè in user ho già specificato che sia falso
        // user.setActive(false);
        user.setActivationCode(UUID.randomUUID().toString());

        mailNotificationService.sendActivationEmail(user);
        return userRepository.save(user);
    }

    public User activate (SignupActivationDTO signupActivationDTO) throws Exception{
        User user = userRepository.findByActivationCode(signupActivationDTO.getActivationCode());
        if (user == null) throw new Exception("User not found");
        user.setActive(true);
        user.setActivationCode(null);
        return userRepository.save(user);
    }
}
