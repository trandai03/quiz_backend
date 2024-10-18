package org.do_an.quiz_java.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.do_an.quiz_java.dto.CompetitionDTO;
import org.do_an.quiz_java.exceptions.DataNotFoundException;
import org.do_an.quiz_java.model.Competition;
import org.do_an.quiz_java.model.User;
import org.do_an.quiz_java.repositories.CompetitionRepository;
import org.do_an.quiz_java.respones.competition.CompetitionResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CompetitionService {
    private final CompetitionRepository competitionRepository;

    private final QuizService quizService;
    public List<CompetitionResponse> findAll() {
        return competitionRepository.findAll().stream().map(CompetitionResponse::fromEntity).collect(Collectors.toList());
    }
    public CompetitionResponse findById(Integer id) {
        return CompetitionResponse.fromEntity(competitionRepository.findById(id).get());
    }

    public CompetitionResponse findByCode(String  code) {
        return CompetitionResponse.fromEntity(competitionRepository.findByCode(code));
    }

    public CompetitionResponse create(CompetitionDTO competitionDTO, User user) throws DataNotFoundException {
        Competition competition = Competition.builder()
                .description(competitionDTO.getDescription())
                .time(competitionDTO.getTime())
                .name(competitionDTO.getName())
                .quiz(quizService.findByQuizId(competitionDTO.getQuizId()))
                .organizedBy(user)
                .startTime(competitionDTO.getStartTime())
                .build();
        return CompetitionResponse.fromEntity(competitionRepository.save(competition));
    }

    public void delete(Integer id) {
        competitionRepository.deleteById(id);
    }

    public CompetitionResponse update(Integer id, CompetitionDTO competitionDTO) throws DataNotFoundException {
        Competition competition = competitionRepository.findById(id).get();
        competition.setDescription(competitionDTO.getDescription());
        competition.setName(competitionDTO.getName());
        competition.setQuiz(quizService.findByQuizId(competitionDTO.getQuizId()));
        competition.setStartTime(competitionDTO.getStartTime());
        competition.setTime(competitionDTO.getTime());
        return CompetitionResponse.fromEntity(competitionRepository.save(competition));
    }
}
