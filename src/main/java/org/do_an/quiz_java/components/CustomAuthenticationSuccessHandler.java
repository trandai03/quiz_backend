package org.do_an.quiz_java.components;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.do_an.quiz_java.model.User;
import org.do_an.quiz_java.repositories.UserRepository;
import org.do_an.quiz_java.respones.user.LoginResponse;
import org.do_an.quiz_java.utils.JwtGenerator;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Lazy
    private final JwtGenerator jwtGenerator;
    @Lazy
    //private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = (String) oAuth2User.getAttributes().get("email");
        String username = (String) oAuth2User.getAttributes().get("name");

        // Generate a more secure password if necessary or handle differently
        String randomPassword = UUID.randomUUID().toString();
        String cryptoPassword = passwordEncoder.encode(randomPassword);

        // Fetch user or create a new one if not found
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = User.builder()
                    .email(email)
                    .username(username)
                    .password(cryptoPassword)  // May not be needed for OAuth2
                    .build();
            return userRepository.save(newUser);
        });

        // Generate JWT token for the user
        String token = jwtGenerator.generateToken(user);

        // Build redirect URI with token parameter
        String uri = UriComponentsBuilder.fromUriString("http://localhost:8080/home")
                .queryParam("token", token)
                //.queryParam("username", user.getUsername())
                .queryParam("email", user.getEmail())
                .build().toUriString();
//        LoginResponse loginResponse = LoginResponse.builder()
//                .username(user.getUsername())
//                .email(user.getEmail())
//                .token(token)
//                .build();
        // Set the response type to JSON and character encoding to UTF-8
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
//
//        // Write LoginResponse as JSON to the response body
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.writeValue(response.getWriter(), loginResponse);
        // Perform the redirect
        getRedirectStrategy().sendRedirect(request, response, uri);

    }
}
