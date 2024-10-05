package org.do_an.quiz_java.dto;

import lombok.ToString;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link org.do_an.quiz_java.model.Quiz}
 */
@Value
@ToString
public class QuizDTO {
    String title;
    String description;
    Integer category_id;
    List<QuestionDTO> questions;

    Boolean isPublished;
}