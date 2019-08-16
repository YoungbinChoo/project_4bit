package com.bitcamp.project.project_4bit.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "roadmap_exercise")
public class RoadmapExercise implements Serializable {

    @Id
    @Column(columnDefinition = "BIGINT", name = "exercise_sequence", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long exerciseSequence;

    @Column(name = "exercise_contents")
    private String exerciseContents;

    @Column(name = "exercise_answer")
    private String exerciseAnswer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "roadmap_stage_no")
    private Roadmap roadmap;

    public Long getExerciseSequence() {
        return exerciseSequence;
    }

    public void setExerciseSequence(Long exerciseSequence) {
        this.exerciseSequence = exerciseSequence;
    }

    public String getExerciseContents() {
        return exerciseContents;
    }

    public void setExerciseContents(String exerciseContents) {
        this.exerciseContents = exerciseContents;
    }

    public String getExerciseAnswer() {
        return exerciseAnswer;
    }

    public void setExerciseAnswer(String exerciseAnswer) {
        this.exerciseAnswer = exerciseAnswer;
    }

    public Roadmap getRoadmap() {
        return roadmap;
    }

    public void setRoadmap(Roadmap roadmap) {
        this.roadmap = roadmap;
    }
}
