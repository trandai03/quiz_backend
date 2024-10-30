package org.do_an.quiz_java.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.do_an.quiz_java.dto.CompetitionDTO;
import org.do_an.quiz_java.dto.QuizDTO;
import org.do_an.quiz_java.exceptions.DataNotFoundException;
import org.do_an.quiz_java.model.Competition;
import org.do_an.quiz_java.model.Quiz;
import org.do_an.quiz_java.model.User;
import org.do_an.quiz_java.repositories.CompetitionRepository;
import org.do_an.quiz_java.respones.competition.CompetitionResponse;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
    private final CompetitionQuizService competititonQuizService;
    @Cacheable(value = "competitions", key = "'findAllCompetitions'")
    public List<CompetitionResponse> findAll() {
        return competitionRepository.findAll().stream().map(CompetitionResponse::fromEntity).collect(Collectors.toList());
    }
    public Competition findById(Integer id) {
        return competitionRepository.findById(id).get();
    }

    public CompetitionResponse findByCode(String  code) {
        return CompetitionResponse.fromEntity(competitionRepository.findByCode(code));
    }
    @Caching(
            put = @CachePut(value = "competitions", key = "'findAllCompetitions'"),
            evict = @CacheEvict(value = "competitions", allEntries = true)
    )
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

    @Caching(
            put = @CachePut(value = "competitions", key = "'findAllCompetitions'"),
            evict = @CacheEvict(value = "competitions", allEntries = true)
    )
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
    @Caching(
            put = @CachePut(value = "competitions", key = "'findAllCompetitions'"),
            evict = @CacheEvict(value = "competitions", allEntries = true)
    )
    public void createQuizForCompetition(User user, Integer competition_id, QuizDTO quizDTO) throws DataNotFoundException {
        Quiz quiz =quizService.save(quizDTO, user);
        competititonQuizService.save(quiz, competition_id);
    }

    @Caching(
            put = @CachePut(value = "competitions", key = "'findAllCompetitions'"),
            evict = @CacheEvict(value = "competitions", allEntries = true)
    )
    public void addQuizForCompetition( Integer competition_id, Quiz quiz) throws DataNotFoundException {
        competititonQuizService.save(quiz, competition_id);
    }

    public List<CompetitionResponse> findByUser(Integer user_id) {
        return CompetitionResponse.fromEntities(competitionRepository.findByOrganizedBy_Id(user_id));
    }
}
