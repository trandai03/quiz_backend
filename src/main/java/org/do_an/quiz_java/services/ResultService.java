package org.do_an.quiz_java.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.do_an.quiz_java.dto.QuestionResultDTO;
import org.do_an.quiz_java.dto.ResultDTO;
import org.do_an.quiz_java.model.*;
import org.do_an.quiz_java.repositories.QuizRepository;
import org.do_an.quiz_java.repositories.ResultRepository;
import org.do_an.quiz_java.respones.result.ResultResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ResultService {
    private  final ResultRepository resultRepository;
    private final QuizRepository quizRepository;
    private final QuestionService questionService;
    private final QuestionResultService questionResultService;
    public List<ResultResponse> getResultByUser(User user){
        List <Result> results =resultRepository.findByUser(user);
        List<ResultResponse> resultResponses = new ArrayList<>();
        for(Result result : results){
            resultResponses.add(ResultResponse.fromEntity(result));
        }

        // Return the list of ResultResponse
        return resultResponses;

    }

    public ResultResponse submit(ResultDTO resultDTO, User user) {
        Quiz quiz = quizRepository.findByQuizId(resultDTO.getQuizId());
        Result result = Result.builder()
                .quiz(quiz)
                .user(user)
                .score(resultDTO.getScore())
                .completedAt(resultDTO.getCompletedAt())
                .submittedTime(resultDTO.getSubmittedTime())
                .build();
        resultRepository.save(result);
        resultRepository.flush();
        List<Question> questions = quiz.getQuestions();
        List<QuestionResultDTO> questionResultDTOS = resultDTO.getQuestionResultDTOS();
        int totalQuestions = questions.size();
        int totalCorrect = 0;
        for (int i = 0; i < totalQuestions; i++) {

            QuestionResultDTO questionResultDTO = questionResultDTOS.get(i);
            Boolean isCorrect =questionService.checkAnswer(questionResultDTO.getQuestionId(), questionResultDTO.getIsSelected());

            if (isCorrect) {
                totalCorrect++;
            }
            QuestionResult questionResult = QuestionResult.builder()
                    .question(questions.get(i))
                    .isCorrect(isCorrect)
                    .selectedChoiceId(questionResultDTO.getIsSelected())
                    .result(result)
                    .build();
            questionResultService.save(questionResult);

        }
        if(totalCorrect != resultDTO.getScore()) {
            log.error("Error while calculating score");
        }

        return ResultResponse.fromEntity(result);
    }
}
