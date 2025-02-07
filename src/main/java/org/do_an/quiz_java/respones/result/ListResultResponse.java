package org.do_an.quiz_java.respones.result;

import lombok.*;
import org.do_an.quiz_java.model.Result;
import org.do_an.quiz_java.respones.competition.CompetitionResponse;

import java.util.List;

import static org.do_an.quiz_java.respones.result.ResultResponse.fromEntityListPreview;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ListResultResponse {
    CompetitionResponse competitionResponse;
    List<ResultResponse> resultResponses;

    public static ListResultResponse fromEntity(List<Result> results) {
        return ListResultResponse.builder()
                .resultResponses(fromEntityListPreview(results))
                .competitionResponse(results.get(0).getCompetition() != null ? CompetitionResponse.fromEntity(results.get(0).getCompetition()) : null)
                .build();
    }
}
