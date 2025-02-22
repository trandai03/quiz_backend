package org.do_an.quiz_java.controllers;

import lombok.RequiredArgsConstructor;
import org.do_an.quiz_java.dto.UpdateResultEssayDTO;
import org.do_an.quiz_java.model.User;
import org.do_an.quiz_java.respones.Response;
import org.do_an.quiz_java.respones.result.ListResultResponse;
import org.do_an.quiz_java.respones.result.ResultResponse;
import org.do_an.quiz_java.services.ResultService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.v1.prefix:/api/v1}/result")
@RequiredArgsConstructor
public class ResultController {
    private final ResultService resultService;

    @GetMapping("/user")
    public List<ResultResponse> getResultByUser(@AuthenticationPrincipal User user){
        return resultService.getResultByUser(user);
    }

    @GetMapping("/competition/{competitionId}")
    public ListResultResponse getAllResultByCompetition(@PathVariable Integer competitionId){
        return resultService.getResultByCompetition(competitionId);
    }

    @GetMapping("/{resultId}")
    public ResultResponse getResultById(@PathVariable Integer resultId){
        return resultService.getResultById(resultId);
    }
    @GetMapping("")
    public String test(@AuthenticationPrincipal User user){
        return user.getUsername();
    }

    @GetMapping("/competition/user")
    public List<ResultResponse> getResultCompetitionByUser(@AuthenticationPrincipal User user){
        return resultService.getResultCompetitionByUser(user);
    }

    @PutMapping("")
    public ResultResponse updateResult(@RequestBody UpdateResultEssayDTO updateResultEssayDTO){
        return resultService.updateResult(updateResultEssayDTO);
    }
}
