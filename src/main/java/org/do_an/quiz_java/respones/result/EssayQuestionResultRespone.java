package org.do_an.quiz_java.respones.result;

import lombok.*;
import org.do_an.quiz_java.model.UserEssayAnswer;
import org.do_an.quiz_java.respones.question.EssayQuestionResponse;

import java.util.ArrayList;
import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EssayQuestionResultRespone {
    Integer id;
//    EssayQuestionResponse question;
    String answer;
    Float score;
    String feedback;
    public static EssayQuestionResultRespone fromEntity(UserEssayAnswer userEssayAnswer){
        return EssayQuestionResultRespone.builder()
                .id(userEssayAnswer.getId())
//                .question(EssayQuestionResponse.fromEntity(userEssayAnswer.getQuestion()))
                .answer(userEssayAnswer.getUserAnswer())
                .score(userEssayAnswer.getScore())
                .feedback(userEssayAnswer.getFeedback())
                .build();
    }

    public static List<EssayQuestionResultRespone> fromEntityList(List<UserEssayAnswer> userEssayAnswers){
        List<EssayQuestionResultRespone> essayQuestionResultRespones = new ArrayList<>();
        for (UserEssayAnswer userEssayAnswer : userEssayAnswers){
            essayQuestionResultRespones.add(EssayQuestionResultRespone.fromEntity(userEssayAnswer));
        }
        return essayQuestionResultRespones;
    }

}
