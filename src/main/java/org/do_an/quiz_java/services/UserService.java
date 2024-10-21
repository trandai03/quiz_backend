package org.do_an.quiz_java.services;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.do_an.quiz_java.dto.UpdatePasswordDTO;
import org.do_an.quiz_java.dto.UpdateUserDTO;
import org.do_an.quiz_java.dto.UserDTO;
import org.do_an.quiz_java.exceptions.DataNotFoundException;
import org.do_an.quiz_java.model.User;
import org.do_an.quiz_java.repositories.TokenRepository;
import org.do_an.quiz_java.repositories.UserRepository;
import org.do_an.quiz_java.utils.JwtGenerator;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService  {
    private final TokenRepository tokenRepository;
//    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtGenerator jwtGenerator;
    //private final RestTemplate restTemplate;
//    @Transactional(readOnly = true)
//    public Page<UserResponse> getAllUsers(Pageable pageable) {
//        Page<User> users = userRepository.findAll(pageable);
//
//        Page<UserResponse> result = users.map(user -> modelMapper.map(user, UserResponse.class));
//        return result;
//    }


    @Transactional
    public User createUser(UserDTO userDTO) throws Exception {
        String username = userDTO.getUsername();

        // Check if username already exists
        if (userRepository.existsByUsername(username)) {
            throw new DataIntegrityViolationException("Username already exists");
        }


        // Create User entity
        User newUser = User.builder()
                .email(userDTO.getEmail())
                .username(username)
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .build();

        // Save User entity to generate ID
        newUser = userRepository.save(newUser);

        return userRepository.save(newUser);
    }
    @Transactional
    public String login(String username, String password) throws Exception {
        User userExist = userRepository.findByUsername(username)
                .orElseThrow(() -> new DataNotFoundException("User not exist")); // 1


        if (!passwordEncoder.matches(password, userExist.getPassword())) {
            throw new BadCredentialsException("Password not match");
        }


        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                username, password, userExist.getAuthorities()
        );

        authenticationManager.authenticate(authenticationToken);

        String token = jwtGenerator.generateToken(userExist);

        return token;
    }

    @Transactional
    public User getUserDetailFromToken(String token) throws Exception {
        String username = jwtGenerator.extractUsername(token);

        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new DataNotFoundException("User not found"));

        return user;
    }

    @Transactional(rollbackFor = Exception.class)
    public User updateInfo(Integer userId, UpdateUserDTO updatedUserDTO) throws Exception {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


//        // update user
//        if (updatedUserDTO.getFullname() != null) {
//            existingUser.setFullname(updatedUserDTO.getFullname());
//        }
//
//        if (updatedUserDTO.getPhoneNumber() != null) {
//            existingUser.setPhoneNumber(updatedUserDTO.getPhoneNumber());
//        }
//
//        if (updatedUserDTO.getAddress() != null) {
//            existingUser.setAddress(updatedUserDTO.getAddress());
//        }

        if (updatedUserDTO.getEmail() != null) {
            existingUser.setEmail(updatedUserDTO.getEmail());
        }

        if (updatedUserDTO.getUsername() != null) {
            existingUser.setUsername(updatedUserDTO.getUsername());
        }

        return userRepository.save(existingUser);

    }
    @Transactional(rollbackFor = Exception.class)
    public User updatePassword(Integer userId, UpdatePasswordDTO updatePasswordDTO) throws Exception {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!passwordEncoder.matches(updatePasswordDTO.getPassword(), existingUser.getPassword())) {
            throw new BadCredentialsException("Password not match");
        }
        if (updatePasswordDTO.getNewPassword() != null
                && !updatePasswordDTO.getNewPassword().isEmpty()) {
            if (!updatePasswordDTO.getNewPassword().equals(updatePasswordDTO.getRetypePassword())) { // check retype password
                throw new DataNotFoundException("Password not match");
            }

            String newPassword = updatePasswordDTO.getNewPassword();
            String encodePassword = passwordEncoder.encode(newPassword);
            existingUser.setPassword(encodePassword);
        }
        return userRepository.save(existingUser);

    }
    @Transactional
    public User deleteUserByUserId(Integer userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        boolean isAdmin = user.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.toString().equals("ROLE_ADMIN"));
        if (isAdmin) {
            throw new IllegalStateException("Cannot delete admin account");
        }


        userRepository.delete(user);
        return user;


    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    private void sendEmail(User user, String subject, String template, String urlAttribute, String urlPath) throws MessagingException {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("username", user.getUsername());
        attributes.put(urlAttribute, "http://" + "localhost:8080" + urlPath);
        emailService.sendMessageHtml(user.getEmail(), subject, template, attributes);
    }

//    public String registerUser(User user, String captcha, String password2) {
//        String url = String.format(captchaUrl, secret, captcha);
//        restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponse.class);
//
//        if (user.getPassword() != null && !user.getPassword().equals(password2)) {
//            throw new PasswordException(PASSWORDS_DO_NOT_MATCH);
//        }
//
//        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
//            throw new EmailException(EMAIL_IN_USE);
//        }
//        user.setActive(false);
//        user.setRoles(Collections.singleton(Role.USER));
//        user.setProvider(AuthProvider.LOCAL);
//        user.setActivationCode(UUID.randomUUID().toString());
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        userRepository.save(user);
//
//        sendEmail(user, "Activation code", "registration-template", "registrationUrl", "/activate/" + user.getActivationCode());
//        return "User successfully registered.";
//    }

}
