package org.do_an.quiz_java.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.do_an.quiz_java.dto.CompetitionDTO;
import org.do_an.quiz_java.exceptions.DataNotFoundException;
import org.do_an.quiz_java.model.Competition;
import org.do_an.quiz_java.model.User;
import org.do_an.quiz_java.repositories.CompetitionRepository;
import org.do_an.quiz_java.respones.competition.CompetitionResponse;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(value = "competitions", key = "#root.methodName")
    public List<CompetitionResponse> findAll() {
        return competitionRepository.findAll().stream().map(CompetitionResponse::fromEntity).collect(Collectors.toList());
    }
    public Competition findById(Integer id) {
        return competitionRepository.findById(id).get();
    }

    public CompetitionResponse findByCode(String  code) {
        return CompetitionResponse.fromEntity(competitionRepository.findByCode(code));
    }
    @CachePut(value = "competitions", key = "#root.methodName")
    public CompetitionResponse create(CompetitionDTO competitionDTO, User user) throws DataNotFoundException {
        Competition competition = Competition.builder()
                .description(competitionDTO.getDescription())
                .time(competitionDTO.getTime())
                .name(competitionDTO.getName())
                .organizedBy(user)
                .startTime(competitionDTO.getStartTime())
                .build();
        competitionRepository.save(competition);
        return CompetitionResponse.fromEntity(competitionRepository.findById(competition.getId()).get());
    }
    @CacheEvict(value = "competitions", allEntries = true)
    public void delete(Integer id) {
        competitionRepository.deleteById(id);
    }

    @CachePut(value = "competitions", key = "#root.methodName")
    public CompetitionResponse update(Integer id, CompetitionDTO competitionDTO) throws DataNotFoundException {
        Competition competition = competitionRepository.findById(id).get();
        competition.setDescription(competitionDTO.getDescription());
        competition.setName(competitionDTO.getName());
        competition.setStartTime(competitionDTO.getStartTime());
        competition.setTime(competitionDTO.getTime());
        return CompetitionResponse.fromEntity(competitionRepository.save(competition));
    }

    @CacheEvict(value = "competitions", allEntries = true)
    public void clearAllQuizCache() {
        System.out.println("Clearing all competition cache...");
    }
}
