package org.do_an.quiz_java.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.do_an.quiz_java.dto.QuestionResultDTO;
import org.do_an.quiz_java.dto.ResultDTO;
import org.do_an.quiz_java.model.*;
import org.do_an.quiz_java.repositories.CompetitionRepository;
import org.do_an.quiz_java.repositories.QuestionChoiceRepository;
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

public class ResultService {
    private final QuestionChoiceRepository questionChoiceRepository;
    private final CompetitionRepository competitionRepository;
    private  final ResultRepository resultRepository;
    private final QuizRepository quizRepository;
    private final QuestionService questionService;
    //private final CompetitionService competitionService;
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
    @Transactional
    public ResultResponse submit(ResultDTO resultDTO, User user) {
        // Lấy quiz từ cơ sở dữ liệu
        Quiz quiz = quizRepository.findByQuizId(resultDTO.getQuizId());
        List<Question> questions = quiz.getQuestions();
        List<QuestionResultDTO> questionResultDTOS = resultDTO.getQuestionResultDTOS();
        List<QuestionResult> questionResults = new ArrayList<>();
        int totalQuestions = questions.size();
        int totalCorrect = 0;

        // Duyệt qua từng câu hỏi trong bài quiz
        for (int i = 0; i < totalQuestions; i++) {
            QuestionResultDTO questionResultDTO = questionResultDTOS.get(i);
            Question question = questionService.findById(questionResultDTO.getQuestionId());

            // Lấy danh sách đáp án đúng cho câu hỏi hiện tại
            List<Integer> correctChoiceIds = questionService.getCorrectAnswerChoices(question.getId());

            // Lấy danh sách các đáp án đã chọn bởi người dùng
            List<Integer> selectedChoiceIds = questionResultDTO.getSelectedChoiceIds();

            // Kiểm tra xem người dùng có chọn đúng tất cả các đáp án không
            boolean isCorrect = selectedChoiceIds.size() == correctChoiceIds.size()
                    && selectedChoiceIds.containsAll(correctChoiceIds);

            // Nếu đáp án đúng, tăng biến đếm số câu đúng
            if (isCorrect) {
                totalCorrect++;
            }
//
//            for(Integer selectedChoiceId : selectedChoiceIds){
//                SelectedChoice selectedChoice = SelectedChoice.builder()
//                        .choice(QuestionChoice.builder().id(selectedChoiceId).build())
//                        .build();
//                selectedChoices.add(selectedChoice);
//            }
            // Tạo đối tượng QuestionResult để lưu kết quả câu hỏi
            QuestionResult questionResult = QuestionResult.builder()
                    .question(question)
                    .isCorrect(isCorrect)
                    .build();

            // Lưu các đáp án đã chọn vào bảng selected_choices
            List<SelectedChoice> selectedChoices = new ArrayList<>();
            for (Integer selectedChoiceId : selectedChoiceIds) {
                SelectedChoice selectedChoice = SelectedChoice.builder()
                        .questionResult(questionResult)
                        .choice(questionChoiceRepository.findById(selectedChoiceId).get()) // Thiết lập đáp án đã chọn
                        .build();
                selectedChoices.add(selectedChoice); // Thêm vào danh sách selectedChoices
            }
            questionResult.setSelectedChoices(selectedChoices); // Lưu danh sách selectedChoices vào questionResult
            // Thêm kết quả câu hỏi vào danh sách kết quả chung
            questionResults.add(questionResult);
        }
        Result result = Result.builder()
                .quiz(quiz)
                .user(user)
                .score(totalCorrect)
                .questionResults(questionResults)
                .submittedTime(resultDTO.getSubmittedTime())
                .build();
        // Tạo đối tượng Result để lưu toàn bộ kết quả quiz
        if(resultDTO.getCompetitionId() != null){
            Competition competition = competitionRepository.findById(resultDTO.getCompetitionId()).get();
            result.setCompetition(competition);
        }

        // Lưu kết quả vào cơ sở dữ liệu
        resultRepository.save(result);

        // Kiểm tra và log nếu điểm tính toán khác với điểm trong DTO
        if (totalCorrect != resultDTO.getScore()) {
            log.warn("Calculated score does not match expected score: expected=" + resultDTO.getScore() + ", actual=" + totalCorrect);
        }

        // Trả về kết quả của bài quiz
        return ResultResponse.fromEntity(result);
    }

    public List<ResultResponse> getResultByCompetition(Integer competitionId) {
        List<Result> results = resultRepository.findByCompetition(competitionRepository.findById(competitionId).get());
        List<ResultResponse> resultResponses = new ArrayList<>();
        for (Result result : results) {
            resultResponses.add(ResultResponse.fromEntity(result));
        }
        return resultResponses;
    }
}
