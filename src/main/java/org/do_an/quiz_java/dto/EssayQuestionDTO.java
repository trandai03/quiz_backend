package org.do_an.quiz_java.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link org.do_an.quiz_java.model.EssayQuestion}
 */
@Value
public class EssayQuestionDTO  {
    @NotNull
    String questionText;
    @NotNull
    String modelAnswer;
    @NotNull
    String scoringCriteria;
    @NotNull
    Boolean isAutoScored;
    @NotNull
    Float maxScore;
}