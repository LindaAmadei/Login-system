package co.example.LoginDemo.user.entities;

import co.example.LoginDemo.auth.repository.UserRepository;
import co.example.LoginDemo.auth.service.LoginService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class JwtTokenFilter {
    @Autowired
    private UserRepository userRepository;


    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException{
        //Get authtorization header and validate
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Barer ")){
    chain.doFilter(request, response);
    return;
        }
        //Get jwt token and validate
        final String token = header.split(" ")[1].trim();

        JWTVerifier verifier = JWT.require(Algorithm.HMAC512(LoginService.JWT_SECRET)).build();
        DecodedJWT decoded = verifier.verify(token);

        if (decoded == null){
            chain.doFilter(request, response);
            return;
        }

        //Get user identity and set it on the spring security context
        Optional<User> userDetails = userRepository.findById(decoded.getClaim("id").asLong());
        if (!userDetails.isPresent() || !userDetails.get().isActive()){
            chain.doFilter(request,response);
            return;
        }

        User user = userDetails.get();
        user.setPassword(null);
        user.setActivationCode(null);
        user.setPasswordResetCode(null);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                user, null, List.of()
        );

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);

    }
}
