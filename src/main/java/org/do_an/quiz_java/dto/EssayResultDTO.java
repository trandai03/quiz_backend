package org.do_an.quiz_java.dto;

import lombok.*;

import java.util.List;
@Value
@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
public class EssayResultDTO {
    Integer quizId;
    List<EssayQuestionResultDTO> essayQuestionResultDTOS;

    Integer submittedTime;
    Integer competitionId = null;
}
