package org.do_an.quiz_java.respones.user;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Data
@Getter
@Setter
@ToString
public class LoginResponse {
    private String name;
    private String email;
    private String token;
}
