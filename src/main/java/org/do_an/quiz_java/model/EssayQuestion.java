package org.do_an.quiz_java.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "essay_questions")
public class EssayQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @NotNull
    @Lob
    @Column(name = "question_text", nullable = false)
    private String questionText;

    @NotNull
    @Lob
    @Column(name = "model_answer", nullable = false)
    private String modelAnswer;

    @NotNull
    @Lob
    @Column(name = "scoring_criteria", nullable = false)
    private String scoringCriteria;

    @NotNull
    @Column(name = "is_auto_scored", nullable = false)
    private Boolean isAutoScored = false;

    @NotNull
    @Column(name = "max_score", nullable = false)
    private Float maxScore;

    @Column(name = "updated_at")
    private Instant updatedAt;

}