package co.example.LoginDemo.auth.controller;

import co.example.LoginDemo.auth.entities.SignupActivationDTO;
import co.example.LoginDemo.auth.entities.SignupDTO;
import co.example.LoginDemo.auth.service.SignupService;
import co.example.LoginDemo.user.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class SignupController {

    @Autowired
    private SignupService signupService;

    @PostMapping("/signup")
    public User signup (@RequestBody SignupDTO signupDTO) throws Exception{
        return signupService.signup(signupDTO);
    }

    @PostMapping("/signup/activation")
    public User signup (@RequestBody SignupActivationDTO signupActivationDTO) throws Exception{
        return signupService.activate(signupActivationDTO);

    }
}
