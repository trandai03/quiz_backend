package org.do_an.quiz_java.respones.result;

import lombok.*;
import org.do_an.quiz_java.model.Question;
import org.do_an.quiz_java.model.QuestionResult;
import org.do_an.quiz_java.model.SelectedChoice;

import java.util.ArrayList;
import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResultResponse {
    Integer id;
    Boolean isCorrect;
    Question question;
    List<SelectedChoice> selectedChoice;
    public static QuestionResultResponse fromEntity(QuestionResult questionResult){
        return QuestionResultResponse.builder()
                .id(questionResult.getId())
                .isCorrect(questionResult.getIsCorrect())
                .question(questionResult.getQuestion())
                .selectedChoice(questionResult.getSelectedChoices())
                .build();
    }

    public static List<QuestionResultResponse> fromEntityList(List<QuestionResult> questionResults){
        List<QuestionResultResponse> questionResultResponses = new ArrayList<>();
        for (QuestionResult questionResult : questionResults){
            questionResultResponses.add(QuestionResultResponse.fromEntity(questionResult));
        }
        return questionResultResponses;
    }
}
