package co.example.LoginDemo.auth.service;

import co.example.LoginDemo.auth.entities.SignupActivationDTO;
import co.example.LoginDemo.auth.entities.SignupDTO;
import co.example.LoginDemo.auth.repository.RoleRepository;
import co.example.LoginDemo.auth.repository.UserRepository;
import co.example.LoginDemo.notification.service.MailNotificationService;
import co.example.LoginDemo.user.entities.User;
import co.example.LoginDemo.user.entities.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class SignupService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MailNotificationService mailNotificationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    public User signup (SignupDTO signupDTO) throws Exception{
        //aggiungo dopo aver fatto autowired roleRepository
        User userInDB = userRepository.findByEmail(signupDTO.getEmail());
        if (userInDB != null) throw new Exception("User alredy exist");

        User user = new User();
        user.setName(signupDTO.getName());
        user.setSurname(signupDTO.getSurname());
        user.setEmail(signupDTO.getEmail());
        user.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
        // cancello perchèè in user ho già specificato che sia falso
        // user.setActive(false);
        user.setActivationCode(UUID.randomUUID().toString());

        //dopo aver aggiunto get e set in "user"
        Set<javax.management.relation.Role> roles = new HashSet<>();
        Optional<Role> userRole = roleRepository.findByName(Roles.REGISTERED);
        if (!userRole.isPresent())throw new Exception("Cannot set role");
        roles.add(userRole.get());
        user.setRoles(roles);

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
