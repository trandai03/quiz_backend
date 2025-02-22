package org.do_an.quiz_java.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link org.do_an.quiz_java.model.Quiz}
 */
@Value
@ToString
@Getter
@Setter
public class QuizDTO {
    @NotNull
    String title;
    @NotNull
    String description;
    @NotNull
    Integer category_id;

    List<QuestionDTO> questions;
    List<EssayQuestionDTO> essayQuestionDTOS;
    @NotNull
    Boolean isPublished;

    @NotNull
    String type;
}