package org.do_an.quiz_java.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @Column(name = "question")
    private String question;

    @Column(name = "created_at")
    private Instant createdAt;

    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<QuestionChoice> questionChoice;

}