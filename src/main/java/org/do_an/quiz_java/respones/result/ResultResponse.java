package org.do_an.quiz_java.respones.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.do_an.quiz_java.model.Competition;
import org.do_an.quiz_java.model.Result;
import org.do_an.quiz_java.respones.competition.CompetitionResponse;

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
    CompetitionResponse competitionResponse;
    List<QuestionResultResponse> resultQuestionResponses;
    List<EssayQuestionResultRespone> essayQuestionResultRespones;
    public static ResultResponse fromEntity(Result result) {
        return ResultResponse.builder()
                .id(result.getId())
                .quizId(result.getQuiz().getId())
                .quizTitle(result.getQuiz().getTitle())
                .score(result.getScore())
                .totalCorrect(result.getTotalCorrect())
                .completedAt(result.getCompletedAt())
                .submittedTime(result.getSubmittedTime())
                .resultQuestionResponses(result.getQuestionResults() != null ? QuestionResultResponse.fromEntityList(result.getQuestionResults()) : null)
                .essayQuestionResultRespones(result.getUserEssayAnswers() != null ? EssayQuestionResultRespone.fromEntityList(result.getUserEssayAnswers()) : null)
                .username(result.getUser().getUsername())
                .competitionResponse(result.getCompetition() != null ? CompetitionResponse.fromEntity(result.getCompetition()) : null)
                .build();
    }
    public static List<ResultResponse> fromEntityList(List<Result> results) {
        return results.stream().map(ResultResponse::fromEntity).toList();
    }

    public static ResultResponse fromEntityPreview(Result result) {
        return ResultResponse.builder()
                .id(result.getId())
                .quizId(result.getQuiz().getId())
                .quizTitle(result.getQuiz().getTitle())
                .score(result.getScore())
                .totalCorrect(result.getTotalCorrect())
                .completedAt(result.getCompletedAt())
                .submittedTime(result.getSubmittedTime())
                .username(result.getUser().getUsername())
                .resultQuestionResponses(result.getQuestionResults() != null ? QuestionResultResponse.fromEntityList(result.getQuestionResults()) : null)
                .essayQuestionResultRespones(result.getUserEssayAnswers() != null ? EssayQuestionResultRespone.fromEntityList(result.getUserEssayAnswers()) : null)
                .build();
    }

    public static List<ResultResponse> fromEntityListPreview(List<Result> results) {
        return results.stream().map(ResultResponse::fromEntityPreview).toList();
    }
}
