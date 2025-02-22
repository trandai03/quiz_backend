package org.do_an.quiz_java.respones.quiz;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.do_an.quiz_java.enums.Type;
import org.do_an.quiz_java.model.Category;
import org.do_an.quiz_java.model.Question;
import org.do_an.quiz_java.model.Quiz;
import org.do_an.quiz_java.model.User;
import org.do_an.quiz_java.respones.category.CategoryResponse;
import org.do_an.quiz_java.respones.question.EssayQuestionResponse;
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
    private List<EssayQuestionResponse> essayQuestions;
    private Boolean isPublished ;
    private CategoryResponse categoryResponse;
    private String usernameCreated;
    private Integer totalQuestions;
    private String image;
    private String type;


    public  static QuizResponse fromEntity(Quiz quiz) {
    return QuizResponse.builder()
            .id(quiz.getId())
            .title(quiz.getTitle())
            .description(quiz.getDescription())
            .createdAt(quiz.getCreatedAt())
            .questions(quiz.getType() == Type.MULTIPLE_CHOICE ? QuestionResponse.fromEntityList(quiz.getQuestions()) : null)
            .essayQuestions(quiz.getType() == Type.ESSAY ? EssayQuestionResponse.fromEntityList(quiz.getEssayQuestions()) : null)
            .isPublished(quiz.getIsPublished())
            .categoryResponse(CategoryResponse.fromEntity(quiz.getCategory()))
            .usernameCreated(quiz.getCreatedBy().getUsername())
            .image(quiz.getImage())
            .totalQuestions(quiz.getTotalQuestions())
            .type(quiz.getType().toString())
            .build();
    }
    public static List<QuizResponse> fromEntities(List<Quiz> quizzes) {
        return quizzes.stream().map(QuizResponse::fromEntity).toList();
    }
    public  static QuizResponse fromEntityPreview(Quiz quiz) {
        return QuizResponse.builder()
                .id(quiz.getId())
                .title(quiz.getTitle())
                .description(quiz.getDescription())
                .createdAt(quiz.getCreatedAt())
                .isPublished(quiz.getIsPublished())
                .usernameCreated(quiz.getCreatedBy().getUsername())
                .totalQuestions(quiz.getTotalQuestions())
                .image(quiz.getImage())
                .categoryResponse(CategoryResponse.fromEntity(quiz.getCategory()))
                .type(quiz.getType().toString())
                .build();
    }
    public static List<QuizResponse> fromEntitiesPreview(List<Quiz> quizzes) {
        return quizzes.stream().map(QuizResponse::fromEntityPreview).toList();
    }
}
