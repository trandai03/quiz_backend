package org.do_an.quiz_java.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.do_an.quiz_java.dto.CompetitionDTO;
import org.do_an.quiz_java.exceptions.DataNotFoundException;
import org.do_an.quiz_java.model.User;
import org.do_an.quiz_java.respones.competition.CompetitionResponse;
import org.do_an.quiz_java.services.CompetitionService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.v1.prefix:/api/v1}/competitions")
@RequiredArgsConstructor
@Slf4j
public class CompetitionController {
    private final CompetitionService competitionService;

    @GetMapping("/getAll")
    public List<CompetitionResponse> findAll() {
        return competitionService.findAll();
    }

    @GetMapping("/getById/{id}")
    public CompetitionResponse findById(@PathVariable Integer id) {
        return competitionService.findById(id);
    }

    @GetMapping("/getByCode/{code}")
    public CompetitionResponse findById(@PathVariable String code) {
        return competitionService.findByCode(code);
    }

    @PostMapping("/create")
    public CompetitionResponse create(@AuthenticationPrincipal User user, @RequestBody CompetitionDTO competitionDTO) throws DataNotFoundException {
        return competitionService.create(competitionDTO, user);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id) {
        competitionService.delete(id);
    }

    @PutMapping("/update/{id}")
    public CompetitionResponse update(@PathVariable Integer id, @RequestBody CompetitionDTO competitionDTO) throws DataNotFoundException {
        return competitionService.update(id, competitionDTO);
    }
}
