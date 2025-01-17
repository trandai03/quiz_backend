package org.do_an.quiz_java.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "user_essay_answers")
public class UserEssayAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private EssayQuestion question;

    @NotNull
    @Lob
    @Column(name = "user_answer", nullable = false)
    private String userAnswer;

    @Column(name = "score")
    private Float score;

    @Lob
    @Column(name = "feedback")
    private String feedback;

    @Column(name = "created_at")
    private Instant createdAt;

}