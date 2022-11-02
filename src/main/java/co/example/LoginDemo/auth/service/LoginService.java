package co.example.LoginDemo.auth.service;

import co.example.LoginDemo.auth.entities.LoginDTO;
import co.example.LoginDemo.auth.entities.LoginRTO;
import co.example.LoginDemo.auth.repository.UserRepository;
import co.example.LoginDemo.user.entities.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class LoginService {

    //di solito si scrive in propieties
    public static final String JWT_SECRET = "3d1ec000-13b8-4708-a393-b929443e19bf";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public LoginRTO login(LoginDTO loginDTO){
        if ((loginDTO == null)) return null;
        User userFromDB = userRepository.findByEmail(loginDTO.getEmail());
        if (userFromDB == null || !userFromDB.isActive())
        return null;

        boolean canLogin = this.canUserLogin(userFromDB, loginDTO.getPassword());
        if (!canLogin) return null;

        String JWT = getJWT(userFromDB);

        userFromDB.setJwtCreatedOn(LocalDateTime.now());
        userRepository.save(userFromDB);

        userFromDB.setPassword(null);
        LoginRTO out = new LoginRTO();
        out.setJWT(JWT);
        out.setUser(userFromDB);


        return out;
    }

    public boolean canUserLogin(User user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    static Date convertToDateViaInstant(LocalDateTime dateToConvert){
        return java.util.Date.from(dateToConvert.atZone(ZoneId.systemDefault()).toInstant());
    }
    public  static String getJWT(User user){
        Date expiresAt = convertToDateViaInstant(LocalDateTime.now().plusDays(15));
        String[] roles = user.getRoles().stream().map(role -> role.getName()).toArray(String[]::new);

        return JWT.create()
                .withIssuer("demo")
                .withIssuedAt(new Date())
                .withExpiresAt(expiresAt)
                .withClaim("roles", String.join(",",roles))
                .withClaim("id", user.getId())
                .sign(Algorithm.HMAC512(JWT_SECRET));;
    }

    public  String generateJwt(User user){
        String JWT = getJWT(user);

        user.setJwtCreatedOn(LocalDateTime.now());
        userRepository.save(user);
        return JWT;
    }
}
