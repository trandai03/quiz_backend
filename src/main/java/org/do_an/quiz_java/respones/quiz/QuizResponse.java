package org.do_an.quiz_java.respones.quiz;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.do_an.quiz_java.model.Category;
import org.do_an.quiz_java.model.Question;
import org.do_an.quiz_java.model.Quiz;
import org.do_an.quiz_java.model.User;
import org.do_an.quiz_java.respones.question.QuestionResponse;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    private List<QuestionResponse> questions;
    private Boolean isPublished ;
    private Category category;
    private String usernameCreated;
    private String image;


    public  static QuizResponse fromEntity(Quiz quiz) {
    return QuizResponse.builder()
            .id(quiz.getId())
            .title(quiz.getTitle())
            .description(quiz.getDescription())
            .createdAt(quiz.getCreatedAt())
            .questions(QuestionResponse.fromEntityList(quiz.getQuestions()))
            .isPublished(quiz.getIsPublished())
            .category(quiz.getCategory())
            .usernameCreated(quiz.getCreatedBy().getUsername())
            .image(quiz.getImage())
            .build();
    }
}
