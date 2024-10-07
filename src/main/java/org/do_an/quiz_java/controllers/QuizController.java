package org.do_an.quiz_java.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.do_an.quiz_java.dto.QuizDTO;
import org.do_an.quiz_java.exceptions.DataNotFoundException;
import org.do_an.quiz_java.model.Question;
import org.do_an.quiz_java.model.Quiz;
import org.do_an.quiz_java.model.User;
import org.do_an.quiz_java.respones.quiz.QuizResponse;
import org.do_an.quiz_java.services.QuestionService;
import org.do_an.quiz_java.services.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${api.v1.prefix:/api/v1}/quizs")
@RequiredArgsConstructor
@Slf4j
public class QuizController {
    @Autowired
    private QuizService quizService;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/getAll")
    public Page<Quiz> findAll(Pageable pageable,
                              @RequestParam(required = false, defaultValue = "false") Boolean published) {

        if (published) {
            return quizService.findAllPublished(pageable);
        } else {
            return quizService.findAll(pageable);
        }
    }
    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    public QuizResponse save(@AuthenticationPrincipal User user, @RequestBody QuizDTO quizDTO) {

//        if(result.hasErrors()){
//            System.out.print("Một hoặc nhiều trường truyền vào không hợp lệ!") ;
//        }
        return quizService.save(quizDTO, user);
    }
    @GetMapping("search")
    public Page<Quiz> searchAll(Pageable pageable, @RequestParam(required = true) String filter,
                                @RequestParam(required = false, defaultValue = "false") Boolean onlyValid) {

        return quizService.search(filter, pageable);
    }

    @GetMapping("/{quiz_id}")
    public QuizResponse find(@PathVariable Integer quiz_id) throws DataNotFoundException {
        Quiz quiz = quizService.findByQuizId(quiz_id);
        QuizResponse quizResponse = QuizResponse.fromEntity(quiz);
        return quizResponse;
    }
//    @GetMapping(value = "/{quiz_id}/questions")
//    @ResponseStatus(HttpStatus.OK)
//    public List<Question> findQuestions(@PathVariable Integer quiz_id,
//                                        @RequestParam(required = false, defaultValue = "false") Boolean onlyValid) throws DataNotFoundException {
//
//        Optional<Quiz> quiz = quizService.find(quiz_id);
//
//        if (onlyValid) {
//            return questionService.findValidQuestionsByQuiz(quiz);
//        } else {
//            return questionService.findQuestionsByQuiz(quiz);
//        }
//
//    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user")
    public List<QuizResponse> findQuizByUser(@AuthenticationPrincipal User user) {
        return quizService.findQuizByUser(user);
    }
    @GetMapping("category/{category_id}")
    public List<QuizResponse> findQuizByCategory(@PathVariable Integer category_id) {
        return quizService.findQuizByCategory(category_id);
    }

    @GetMapping("/getAllQuiz")
    public List<QuizResponse> findAllQuiz() {

        return quizService.findAllQuiz();
    }
}
