package org.do_an.quiz_java.respones.result;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    Float score;
    Integer totalCorrect;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
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
                .totalCorrect(result.getTotalCorrect())
                .completedAt(result.getCompletedAt())
                .submittedTime(result.getSubmittedTime())
                .resultQuestionResponses(QuestionResultResponse.fromEntityList(result.getQuestionResults()))
                .username(result.getUser().getUsername())
                .competitionId(result.getCompetition() != null ? result.getCompetition().getId() : null)
                .build();
    }
}
