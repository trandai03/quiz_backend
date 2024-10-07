package org.do_an.quiz_java.dto;

import lombok.*;

import java.util.List;

@Value
@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
public class ResultDTO {
    Integer quiz_id;
    List<AnswerDTO> answerDTOS;

}
