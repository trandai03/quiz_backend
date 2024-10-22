package org.do_an.quiz_java.respones.result;

import lombok.*;
import org.do_an.quiz_java.model.Result;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResultResponse {
    Integer id;
    Integer quizId;
    String quizTitle;
    Integer score;
    LocalDateTime completedAt;
    Integer submittedTime;
    String username;
    Integer competitionId;
    List<QuestionResultResponse> resultQuestionResponses;
    public static ResultResponse fromEntity(Result result) {
        return ResultResponse.builder()
                .id(result.getId())
                .quizId(result.getQuiz().getId())
                .quizTitle(result.getQuiz().getTitle())
                .score(result.getScore())
                .completedAt(result.getCompletedAt())
                .submittedTime(result.getSubmittedTime())
                .resultQuestionResponses(QuestionResultResponse.fromEntityList(result.getQuestionResults()))
                .username(result.getUser().getUsername())
                .competitionId(result.getCompetition() != null ? result.getCompetition().getId() : null)
                .build();
    }
}
