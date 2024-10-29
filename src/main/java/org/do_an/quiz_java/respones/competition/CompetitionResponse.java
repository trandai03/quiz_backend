package org.do_an.quiz_java.respones.competition;

import lombok.*;
import org.do_an.quiz_java.model.Competition;
import org.do_an.quiz_java.model.Quiz;
import org.do_an.quiz_java.respones.quiz.QuizResponse;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionResponse {
    private String description;
    private Integer time;
    private String name;
    private Integer id;
    private String code;
    private Integer quizId;
    private QuizResponse quizResponse;
    private String organizedBy;
    private String startTime;
    private String title;

    public static CompetitionResponse fromEntity(Competition competition) {
        return CompetitionResponse.builder()
                .id(competition.getId())
                .description(competition.getDescription())
                .time(competition.getTime())
                .name(competition.getName())
                .code(competition.getCode())
                .quizResponse(QuizResponse.fromEntity(competition.getQuiz()))
                .organizedBy(competition.getOrganizedBy().getUsername())
                .startTime(competition.getStartTime().toString())
                .title(competition.getQuiz().getTitle())
                .build();
    }


}
