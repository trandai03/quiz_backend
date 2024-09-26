package org.do_an.quiz_java.controllers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.do_an.quiz_java.model.Quiz;
import org.do_an.quiz_java.services.QuestionService;
import org.do_an.quiz_java.services.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.v1.prefix:/api/v1}/quizs")
@RequiredArgsConstructor
@Slf4j
public class QuizController {
    @Autowired
    private QuizService quizService;

    @Autowired
    private QuestionService questionService;

//    @GetMapping("getAll")
//    public Page<Quiz> findAll(Pageable pageable,
//                              @RequestParam(required = false, defaultValue = "false") Boolean published) {
//
//        if (published) {
//            return quizService.findAllPublished(pageable);
//        } else {
//            return quizService.findAll(pageable);
//        }
//    }
}
