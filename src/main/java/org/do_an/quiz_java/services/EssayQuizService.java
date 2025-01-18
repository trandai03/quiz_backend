package org.do_an.quiz_java.services;

import lombok.RequiredArgsConstructor;
import org.do_an.quiz_java.dto.EssayQuizDTO;
import org.do_an.quiz_java.dto.QuizDTO;
import org.do_an.quiz_java.enums.Type;
import org.do_an.quiz_java.exceptions.DataNotFoundException;
import org.do_an.quiz_java.model.Category;
import org.do_an.quiz_java.model.Quiz;
import org.do_an.quiz_java.model.User;
import org.do_an.quiz_java.repositories.CategoryRepository;
import org.do_an.quiz_java.repositories.EssayQuestionRepository;
import org.do_an.quiz_java.repositories.QuizRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EssayQuizService {
    private final EssayQuestionRepository essayQuestionRepository;
    private final QuizService quizService;
    private final CategoryRepository categoryRepository;
    private final EssayQuestionService essayQuestionService;
    private final QuizRepository quizRepository;
    public Quiz save(QuizDTO quizDTO, User user) throws DataNotFoundException {
        Category category = categoryRepository.findById(quizDTO.getCategory_id())
                .orElseThrow(() -> new DataNotFoundException("Category not found"));
        Quiz quiz = Quiz.builder()
                .title(quizDTO.getTitle())
                .category(category)
                .createdBy(user)
                .description(quizDTO.getDescription())
                .isPublished(quizDTO.getIsPublished())
                .totalQuestions(quizDTO.getEssayQuestionDTOS().size())
                .type(Type.ESSAY)
                .createdBy(user).build();
        //        quizRepository.save(quiz);
        quiz = quizRepository.save(quiz);
        quiz.setEssayQuestions(essayQuestionService.saveAll(quizDTO.getEssayQuestionDTOS(), quiz));
        return quizRepository.save(quiz);
    }

    public Quiz update(QuizDTO quizDTO, User user, Integer quizId) throws DataNotFoundException {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new DataNotFoundException("Quiz not found"));
        Category category = categoryRepository.findById(quizDTO.getCategory_id())
                .orElseThrow(() -> new DataNotFoundException("Category not found"));
        quiz.setTitle(quizDTO.getTitle());
        quiz.setCategory(category);
        quiz.setDescription(quizDTO.getDescription());
        quiz.setIsPublished(quizDTO.getIsPublished());
        quiz.setTotalQuestions(quizDTO.getEssayQuestionDTOS().size());
        quiz.setType(Type.ESSAY);
        quiz.setCreatedBy(user);
        quiz.setEssayQuestions(essayQuestionService.saveAll(quizDTO.getEssayQuestionDTOS(), quiz));
        return quizRepository.save(quiz);
    }
    public
}
