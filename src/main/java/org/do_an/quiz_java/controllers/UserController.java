package org.do_an.quiz_java.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.do_an.quiz_java.dto.UpdateUserDTO;
import org.do_an.quiz_java.dto.UserDTO;
import org.do_an.quiz_java.dto.UserLoginDTO;
import org.do_an.quiz_java.model.Token;
import org.do_an.quiz_java.model.User;
import org.do_an.quiz_java.respones.Response;
import org.do_an.quiz_java.respones.user.LoginResponse;
import org.do_an.quiz_java.respones.user.UserResponse;
import org.do_an.quiz_java.services.TokenService;
import org.do_an.quiz_java.services.UserService;
import org.modelmapper.ModelMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.v1.prefix:/api/v1}/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final TokenService tokenService;
    private final ModelMapper modelMapper;

    @PostMapping("/login")
    public ResponseEntity<Response> login(
            @RequestBody @Valid UserLoginDTO userLoginDTO
    ) {

        try {
            String tokenGenerate = userService.login(userLoginDTO.getUsername(), userLoginDTO.getPassword());

            User userLoginDetail = userService.getUserDetailFromToken(tokenGenerate);

            Token token = tokenService.addToken(userLoginDetail, tokenGenerate); // Save token to database

            LoginResponse loginResponse = modelMapper.map(userLoginDetail, LoginResponse.class);
            loginResponse.setToken(token.getToken());
//            loginResponse.setRole(token.getUser()
//                    .getAuthorities()
//                    .stream()
//                    .map(GrantedAuthority::getAuthority).collect(Collectors.toList()));

            return ResponseEntity.ok().body(new Response("success", "Login successful", loginResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Response("error", e.getMessage(), null));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Response> create(@RequestBody @Valid UserDTO userDTO) {
        try {

            return ResponseEntity.ok().body(new Response("success", "User created successfully", UserResponse.fromUser(userService.createUser(userDTO))));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Response("error", e.getMessage(), null));
        }
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<Response> update(
            @PathVariable Integer userId,
            @RequestBody @Valid UpdateUserDTO updateUserDTO
    ) {
        try {
            User userDTOUpdated = userService.updateInfo(userId, updateUserDTO);


            return ResponseEntity.ok().body(new Response("success", "User updated successfully", UserResponse.fromUser(userDTOUpdated)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Response("error", e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Response> delete(@PathVariable Integer userId) {
        try {
            return ResponseEntity.ok().body(new Response("success", "User deleted successfully", UserResponse.fromUser( userService.deleteUserByUserId(userId))));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Response("error", e.getMessage(), null));
        }
    }
}
