package org.do_an.quiz_java.repositories;

import org.do_an.quiz_java.model.Competition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Integer> {
    Competition findByCode(String code);
}