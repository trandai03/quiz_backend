package org.do_an.quiz_java.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.do_an.quiz_java.chatmodel.Assistant;
import org.do_an.quiz_java.dto.*;
import org.do_an.quiz_java.model.*;
import org.do_an.quiz_java.repositories.*;
import org.do_an.quiz_java.respones.GradingResponse;
import org.do_an.quiz_java.respones.result.ListResultResponse;
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
    private final UserEssayAnswerRepository userEssayAnswerRepository;
    private final Assistant assistant;
    private final QuestionChoiceRepository questionChoiceRepository;
    private final CompetitionRepository competitionRepository;
    private  final ResultRepository resultRepository;
    private final QuizRepository quizRepository;
    private final QuestionService questionService;
    private final QuestionResultService questionResultService;
    private final EssayQuestionService essayQuestionService;
    private final EssayQuestionRepository essayQuestionRepository;
    public List<ResultResponse> getResultByUser(User user){
        List <Result> results =resultRepository.findByUserId(user.getId());
        List<ResultResponse> resultResponses = new ArrayList<>();
        for(Result result : results){
            if(result.getCompetition() != null){
                continue;
            }
            resultResponses.add(ResultResponse.fromEntity(result));
            log.info("Result: " + result);
        }

        // Return the list of ResultResponse
        return resultResponses;

    }
    public List<ResultResponse> getResultCompetitionByUser(User user){
        List <Result> results =resultRepository.findByUserId(user.getId());
        List<ResultResponse> resultResponses = new ArrayList<>();
        for(Result result : results){
            if(result.getCompetition() == null){
                continue;
            }
            resultResponses.add(ResultResponse.fromEntity(result));
            log.info("Result: " + result);
        }

        // Return the list of ResultResponse
        return resultResponses;

    }
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
            //questionResultService.save(questionResult);
        }
        float score = (float) totalCorrect / totalQuestions * 10;
        float roundedScore = Math.round(score * 100) / 100.0f;
        if (totalCorrect != resultDTO.getTotalCorrect() || roundedScore != resultDTO.getScore()) {
            log.warn("Calculated score does not match expected score: expected=" + resultDTO.getTotalCorrect() + ", actual=" + totalCorrect + ", expected=" + resultDTO.getScore() + ", actual=" + roundedScore);
            throw new RuntimeException("Calculated score does not match expected score");
        }
        Result result = Result.builder()
                .quiz(quiz)
                .user(user)
                .totalCorrect(totalCorrect)
                .score(roundedScore)
                .questionResults(questionResults)
                .submittedTime(resultDTO.getSubmittedTime())
                .build();
        for(QuestionResult questionResult : questionResults){
            questionResult.setResult(result);
        }
        questionResultService.saveAll(questionResults);
        // Kiểm tra và log nếu điểm tính toán khác với điểm trong DTO


        // Tạo đối tượng Result để lưu toàn bộ kết quả quiz
        if(resultDTO.getCompetitionId() != null){
            Competition competition = competitionRepository.findById(resultDTO.getCompetitionId()).get();
            result.setCompetition(competition);
        }

        // Lưu kết quả vào cơ sở dữ liệu
        resultRepository.save(result);

        // Trả về kết quả của bài quiz
        return ResultResponse.fromEntity(result);
    }

    public ResultResponse submitEssay(EssayResultDTO essayResultDTO, User user){
        Quiz quiz = quizRepository.findByQuizId(essayResultDTO.getQuizId());
        Result result = Result.builder()
                .user(user)
                .submittedTime(essayResultDTO.getSubmittedTime())
                .quiz(quiz)
                .competition(competitionRepository.findById(essayResultDTO.getCompetitionId()).get())
                .totalCorrect(0)
                .build();
        resultRepository.save(result);
        Float totalScore = 0.0f;
        List<EssayQuestionResultDTO> essayQuestionResultDTOS = essayResultDTO.getEssayQuestionResultDTOS();
        List<UserEssayAnswer> userEssayAnswers = new ArrayList<>();
        for( EssayQuestionResultDTO essayQuestionResultDTO : essayQuestionResultDTOS){
            EssayQuestion essayQuestion = essayQuestionService.findById(essayQuestionResultDTO.getQuestionId());
            

//
            String message = "Câu trả lời : " + essayQuestionResultDTO.getAnswer() + "Điểm tối đa : " + essayQuestion.getMaxScore() + "Câu trả lời mẫu : " + essayQuestion.getModelAnswer() + "Tiêu chí chấm điểm : " + essayQuestion.getScoringCriteria();
            double temperature = 0.2;
            try {
                String aiCheck = assistant.teacher(message, temperature);

                GradingResponse response = GradingResponse.parseGradingResponse(aiCheck);
                System.out.println(response.toString());
                if(response == null){
                    throw new RuntimeException("Lỗi khi gọi API chấm điểm");
                }
                UserEssayAnswer userEssayAnswer = UserEssayAnswer.builder()
                        .user(user)
                        .question(essayQuestion)
                        .userAnswer(essayQuestionResultDTO.getAnswer())
                        .score(response.getScore())
                        .feedback(response.getFeedback())
                        .result(result)
                        .build();
                userEssayAnswerRepository.save(userEssayAnswer);
                userEssayAnswers.add(userEssayAnswer);
                totalScore += response.getScore();
            } catch (NumberFormatException e) {
                throw new RuntimeException("Lỗi khi gọi API chấm điểm : " + e.getMessage());
            }

        }

        result.setUserEssayAnswers(userEssayAnswers);
        result.setScore(totalScore);
        resultRepository.save(result);
        return ResultResponse.fromEntity(result);

    }
    public ListResultResponse getResultByCompetition(Integer competitionId) {
        List<Result> results = resultRepository.findByCompetition(competitionRepository.findById(competitionId).get());

        return ListResultResponse.fromEntity(results);
    }

    public ResultResponse getResultById(Integer resultId) {
        Result result = resultRepository.findById(resultId).get();
        log.info("Result: " + result);
        return ResultResponse.fromEntity(result);
    }

    public List<ResultResponse> getAllResult() {
        List<Result> results = resultRepository.findAll();
        List<ResultResponse> resultResponses = new ArrayList<>();
        for (Result result : results) {
            resultResponses.add(ResultResponse.fromEntity(result));
        }
        return resultResponses;
    }

    public boolean isUserHasResult(User user, Competition competition) {
        return resultRepository.existsByUserAndCompetition(user,competition);
    }

    public ResultResponse updateResult(UpdateResultEssayDTO updateResultEssayDTO) {
        Result result = resultRepository.findById(updateResultEssayDTO.getId()).get();
        result.setScore(updateResultEssayDTO.getTotalScore());
        List<UpdateResultEssayQuestionDTO> updateResultEssayQuestionDTOS = updateResultEssayDTO.getQuestions();
        for(UpdateResultEssayQuestionDTO updateResultEssayQuestionDTO : updateResultEssayQuestionDTOS){
            UserEssayAnswer userEssayAnswer = userEssayAnswerRepository.findById(updateResultEssayQuestionDTO.getId()).get();
            userEssayAnswer.setScore(updateResultEssayQuestionDTO.getScore());
            userEssayAnswer.setFeedback(updateResultEssayQuestionDTO.getFeedback());
            userEssayAnswerRepository.save(userEssayAnswer);
        }
        resultRepository.save(result);
        return ResultResponse.fromEntity(result);
    }
}
