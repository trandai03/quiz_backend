package org.do_an.quiz_java.respones.quiz;

import lombok.*;
import org.do_an.quiz_java.dto.QuestionResultDTO;
import org.do_an.quiz_java.model.Category;
import org.do_an.quiz_java.model.Question;
import org.do_an.quiz_java.model.Result;
import org.do_an.quiz_java.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResultResponse {
    Integer quizId;
    //List<QuestionResultDTO> questionResultDTOS;
    Integer score;
    LocalDateTime completedAt;
    Integer submittedTime;
    String username;
    public static ResultResponse fromEntity(Result result) {
        return ResultResponse.builder()
                .quizId(result.getQuiz().getId())
                .score(result.getScore())
                .completedAt(result.getCompletedAt())
                .submittedTime(result.getSubmittedTime())
                .username(result.getUser().getUsername())
                .build();
    }
}
