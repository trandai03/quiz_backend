package org.do_an.quiz_java.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.do_an.quiz_java.dto.QuestionResultDTO;
import org.do_an.quiz_java.dto.QuizDTO;
import org.do_an.quiz_java.dto.ResultDTO;
import org.do_an.quiz_java.exceptions.DataNotFoundException;
import org.do_an.quiz_java.model.*;
import org.do_an.quiz_java.repositories.CategoryRepository;
import org.do_an.quiz_java.repositories.QuizRepository;
import org.do_an.quiz_java.respones.quiz.QuizResponse;
import org.do_an.quiz_java.respones.result.ResultResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class QuizService {
    private final ModelMapper modelMapper;

    private final QuizRepository quizRepository;
    private final QuestionService questionService;
    private final CategoryService categoryService;
    private final QuestionResultService questionResultService;
    private final ResultService resultService;
    private final CloudinaryService cloudinaryService;
    private final CategoryRepository categoryRepository;
    public QuizResponse save(QuizDTO quizDTO,  User user) {
        Quiz quiz = Quiz.builder()
                .title(quizDTO.getTitle())
                .category(categoryService.find(quizDTO.getCategory_id()))
                .createdBy(user)
                .description(quizDTO.getDescription())
                .isPublished(quizDTO.getIsPublished())
                .totalQuestions(quizDTO.getQuestions().size())
                .createdBy(user).build();
//        quizRepository.save(quiz);

        questionService.save(quizDTO.getQuestions(),quiz);
        quiz = quizRepository.save(quiz);
        return QuizResponse.fromEntity(quiz);
    }
    public QuizResponse updateQuizWithImage(MultipartFile file, Integer quizId) throws DataNotFoundException {
        Quiz quiz = findByQuizId(quizId);
        try {
            String imageUrl = storeFile(file);
            log.error("Error while uploading image" + imageUrl);
            quiz.setImage(imageUrl);
        } catch (Exception e) {
            log.error("Error while uploading image");
        }
        quizRepository.save(quiz);
        return QuizResponse.fromEntity(quiz);
    }

    public Page<Quiz> findAll(Pageable pageable) {
        return quizRepository.findAll(pageable);
    }

    public Quiz findByQuizId(Integer id) throws DataNotFoundException {

        return quizRepository.findByQuizId(id);
    }

    public Page<Quiz> findAllPublished(Pageable pageable) {
        return quizRepository.findByIsPublishedTrue(pageable);
    }


    //    public Quiz update(Quiz newQuiz) throws TokenExpiredException, DataNotFoundException {
//        Optional<Quiz> currentQuiz = find(newQuiz.getId());
//
//        mergeQuizzes(currentQuiz, newQuiz);
//        return quizRepository.save(currentQuiz);
//    }
    private void mergeQuizzes(Quiz currentQuiz, Quiz newQuiz) {
        currentQuiz.setTitle(newQuiz.getTitle());
        currentQuiz.setDescription(newQuiz.getDescription());
    }

    public Page<Quiz> search(String query, Pageable pageable) {
        return quizRepository.searchByName(query, pageable);
    }

    public List<QuizResponse> findQuizByUser(User user) {
        return quizRepository.findByCreatedBy(user).stream()
                .map(QuizResponse::fromEntity)
                .collect(Collectors.toList());

    }
    public List<QuizResponse> findQuizByCategory(Integer categoryId) {
        return quizRepository.findByCategory(categoryService.find(categoryId)).stream()
                .map(QuizResponse::fromEntity)
                .collect(Collectors.toList());

    }
    public List<QuizResponse> findAllQuiz() {
        return quizRepository.findAll().stream()
                .map(QuizResponse::fromEntity)
                .collect(Collectors.toList());

    }
    private String storeFile(MultipartFile file) throws Exception {
        if(file.getSize() > 10 * 1024 * 1024) { // Kích thước > 10MB
            return "False";
        }
        String contentType = file.getContentType();
        if(contentType == null || !contentType.startsWith("image/")) {
            return "False";
        }
        String folderName = "image";
        Map<String, Object> uploadResult = cloudinaryService.uploadFile(file, folderName);
        return uploadResult.get("url").toString();

    }
    public void delete(Integer id) {
        quizRepository.deleteById(id);
    }
    public ResultResponse submit(ResultDTO resultDTO, User user) {
        return resultService.submit(resultDTO,user);
    }

    public QuizResponse update(QuizDTO quizDTO, Integer quizId) throws DataNotFoundException {
        Quiz existingQuiz = findByQuizId(quizId);
        Category category = categoryRepository.findById(quizDTO.getCategory_id())
                .orElseThrow(() -> new DataNotFoundException("Category not found"));

        if(existingQuiz == null) {
            throw new DataNotFoundException("Quiz not found");
        }
        if (quizDTO.getTitle() != null){
            existingQuiz.setTitle(quizDTO.getTitle());
        }

        if (quizDTO.getDescription() != null){
            existingQuiz.setDescription(quizDTO.getDescription());
        }
        if(quizDTO.getIsPublished() != null){
            existingQuiz.setIsPublished(quizDTO.getIsPublished());
        }
        if(quizDTO.getQuestions() != null){
            existingQuiz.setTotalQuestions(quizDTO.getQuestions().size());
        }
        existingQuiz.setCategory(category);

        quizRepository.save(existingQuiz);
        questionService.update(quizDTO.getQuestions(),existingQuiz);
        return QuizResponse.fromEntity(existingQuiz);
    }

    public QuizResponse publishQuiz(Integer quizId) throws DataNotFoundException {
        Quiz quiz = findByQuizId(quizId);
        quiz.setIsPublished(true);
        quizRepository.save(quiz);
        return QuizResponse.fromEntity(quiz);
    }

    public QuizResponse unPublishQuiz(Integer quizId) throws DataNotFoundException {
        Quiz quiz = findByQuizId(quizId);
        quiz.setIsPublished(false);
        quizRepository.save(quiz);
        return QuizResponse.fromEntity(quiz);
    }
}
