package org.do_an.quiz_java.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
@Tag(name = "Root", description = "Root for testing the API")
@Hidden
public class HomeController {
    @GetMapping("/test")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String home() {
        return """
                Welcome to FoodOrdering!:)
                Please check the API documentation at /api-docs
                Please check the API documentation at /swagger-ui.html
                Please check the API documentation at /redoc.html
                """;
    }
}
