package org.do_an.quiz_java.respones.competition;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.do_an.quiz_java.model.Competition;
import org.do_an.quiz_java.model.Quiz;
import org.do_an.quiz_java.respones.quiz.QuizResponse;

import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class CompetitionResponse {
    private String description;
    private Integer time;
    private String name;
    private Integer id;
    private String code;
    private List<CompetitionQuizResponse> competitionQuizResponses;
    private String organizedBy;
    private String startTime;

    public static CompetitionResponse fromEntity(Competition competition) {
        for(CompetitionQuizResponse competitionQuizResponse : CompetitionQuizResponse.fromEntities(competition.getCompetitionQuizzes())) {
            log.info("CompetitionQuizResponse: " + competitionQuizResponse);
        }
        return CompetitionResponse.builder()
                .id(competition.getId())
                .description(competition.getDescription())
                .time(competition.getTime())
                .name(competition.getName())
                .code(competition.getCode())
                .competitionQuizResponses(CompetitionQuizResponse.fromEntities(competition.getCompetitionQuizzes()))
                .organizedBy(competition.getOrganizedBy().getUsername())
                .startTime(competition.getStartTime().toString())
                .build();
    }


}
