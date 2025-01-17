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

        quiz.setEssayQuestions(essayQuestionService.saveAll(quizDTO.getEssayQuestionDTOS()));
        return quizRepository.save(quiz);
    }
}
