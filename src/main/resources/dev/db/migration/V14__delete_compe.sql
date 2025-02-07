ALTER TABLE competition_quiz DROP FOREIGN KEY fk_competition;
ALTER TABLE competition_quiz ADD CONSTRAINT fk_competition
    FOREIGN KEY (competition_id) REFERENCES competition(id)
        ON DELETE CASCADE;

ALTER TABLE results DROP FOREIGN KEY fk_results_competition;
ALTER TABLE results ADD CONSTRAINT fk_results_competition
    FOREIGN KEY (competition_id) REFERENCES competition(id)
        ON DELETE CASCADE;

ALTER TABLE user_essay_answers DROP FOREIGN KEY user_essay_answers_ibfk_2;
ALTER TABLE user_essay_answers ADD CONSTRAINT user_essay_answers_ibfk_2
    FOREIGN KEY (question_id) REFERENCES essay_questions(id)
        ON DELETE CASCADE;
