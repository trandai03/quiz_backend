package org.do_an.quiz_java.services;

import lombok.RequiredArgsConstructor;
import org.do_an.quiz_java.dto.EssayQuestionDTO;
import org.do_an.quiz_java.model.EssayQuestion;
import org.do_an.quiz_java.repositories.EssayQuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EssayQuestionService {
    private final EssayQuestionRepository essayQuestionRepository;

    public EssayQuestion save(EssayQuestionDTO essayQuestionDTO) {
        EssayQuestion essayQuestion = EssayQuestion.builder().
                questionText(essayQuestionDTO.getQuestionText())
                .isAutoScored(essayQuestionDTO.getIsAutoScored())
                .modelAnswer(essayQuestionDTO.getModelAnswer())
                .maxScore(essayQuestionDTO.getMaxScore())
                .scoringCriteria(essayQuestionDTO.getScoringCriteria())
                .build();
        return essayQuestionRepository.save(essayQuestion);
    }
    public List<EssayQuestion> saveAll(List<EssayQuestionDTO> essayQuestionDTOs) {
        List<EssayQuestion> essayQuestions = essayQuestionDTOs.stream().map((element) -> EssayQuestion.builder()
                .questionText(element.getQuestionText())
                .isAutoScored(element.getIsAutoScored())
                .modelAnswer(element.getModelAnswer())
                .maxScore(element.getMaxScore())
                .scoringCriteria(element.getScoringCriteria())
                .build()).collect(Collectors.toList());
        return essayQuestionRepository.saveAll(essayQuestions);
    }
}
