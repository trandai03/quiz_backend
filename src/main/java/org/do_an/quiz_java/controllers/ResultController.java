package org.do_an.quiz_java.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.v1.prefix:/api/v1}/result")
@RequiredArgsConstructor
public class ResultController {

}
