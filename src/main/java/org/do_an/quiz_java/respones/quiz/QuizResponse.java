package org.do_an.quiz_java.respones.quiz;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.do_an.quiz_java.model.Category;
import org.do_an.quiz_java.model.Question;
import org.do_an.quiz_java.model.Quiz;
import org.do_an.quiz_java.model.User;

import java.time.LocalDateTime;
import java.util.List;
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuizResponse {
    private String description;
    private String title;
     private Integer id;
    private LocalDateTime createdAt;
    private List<Question> questions;
    private Boolean isPublished ;
    private Category category;
    private String usernameCreated;


    public  static QuizResponse fromEntity(Quiz quiz) {
    return QuizResponse.builder()
            .id(quiz.getId())
            .title(quiz.getTitle())
            .description(quiz.getDescription())
            .createdAt(quiz.getCreatedAt())
            .questions(quiz.getQuestions())
            .isPublished(quiz.getIsPublished())
            .category(quiz.getCategory())
            .usernameCreated(quiz.getCreatedBy().getUsername())
            .build();
    }
}
