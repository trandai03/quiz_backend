package org.do_an.quiz_java.respones.user;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.do_an.quiz_java.model.User;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Builder
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    //@Hidden
    private int id;
    private String fullname;
    private String phoneNumber;
    private LocalDateTime dateOfBirth;
    private String username;
    private String email;
    public static UserResponse fromUser(User user){
       if(user == null){
           log.error("UserResponse {}", (Object) null);
           return null;
       }


        return UserResponse.builder()
//                .roles(user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList()))
                .id(user.getId())
                .username(user.getUsername())
                .dateOfBirth(user.getDateOfBirth())
                .fullname(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .build();
    }


}
