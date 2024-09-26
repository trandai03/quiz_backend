package org.do_an.quiz_java.repositories;

import org.do_an.quiz_java.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends JpaRepository<Result, Integer> {
}