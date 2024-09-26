package org.do_an.quiz_java.repositories;

import org.do_an.quiz_java.model.Token;
import org.do_an.quiz_java.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Token findByToken(String token);
    Token findByUser(User user);

    long deleteByUser(User user);
}