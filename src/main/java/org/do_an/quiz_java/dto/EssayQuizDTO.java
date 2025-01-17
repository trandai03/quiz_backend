package org.do_an.quiz_java.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.util.List;
@Value
public class EssayQuizDTO {
    @NotNull
    String title;
    @NotNull
    String description;
    @NotNull
    Integer category_id;
    @NotNull
    List<EssayQuestionDTO> essayQuestionDTOS;
    @NotNull
    Boolean isPublished;
}
