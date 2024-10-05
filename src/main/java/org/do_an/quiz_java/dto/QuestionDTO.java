package org.do_an.quiz_java.dto;

import lombok.ToString;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

/**
 * DTO for {@link org.do_an.quiz_java.model.Question}
 */
@Value
@ToString
public class QuestionDTO  {
    Integer quiz_id;
    String question;
    List<QuestionChoiceDTO> questionChoiceDTOS;

}