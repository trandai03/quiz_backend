package org.do_an.quiz_java.respones.question;

import lombok.*;
import org.do_an.quiz_java.model.EssayQuestion;

import java.util.ArrayList;
import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EssayQuestionResponse {
    Integer id;
    private String question;
    private String modelAnswer;
    private String scoringCriteria;
    private Boolean isAutoScored;
    private Float maxScore;

    public static EssayQuestionResponse fromEntity(EssayQuestion essayQuestion) {
        return EssayQuestionResponse.builder()
                .id(essayQuestion.getId())
                .question(essayQuestion.getQuestionText())
                .modelAnswer(essayQuestion.getModelAnswer())
                .scoringCriteria(essayQuestion.getScoringCriteria())
                .isAutoScored(essayQuestion.getIsAutoScored())
                .maxScore(essayQuestion.getMaxScore())
                .build();
    }

    public static List<EssayQuestionResponse> fromEntityList(List<EssayQuestion> essayQuestions) {
        List<EssayQuestionResponse> essayQuestionResponses = new ArrayList<>();
        for (EssayQuestion essayQuestion : essayQuestions) {
            essayQuestionResponses.add(EssayQuestionResponse.fromEntity(essayQuestion));
        }
        return essayQuestionResponses;
    }
}
