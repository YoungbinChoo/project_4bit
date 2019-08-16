package com.bitcamp.project.project_4bit.entity;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "quiz")
@DynamicInsert
public class Quiz implements Serializable {

    @Id
    @Column(name = "quiz_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quizId;

    @Column(name = "quiz_contents")
    private String quizContents;

    @Column(name = "quiz_answer")
    private String quizAnswer;

    @Column(name = "quiz_each_score")
    private int quizEachScore;

    @Column(name = "quiz_subject")
    private String quizSubject;

    @Column(name = "quiz_chapter")
    private String quizChapter;

    @Column(name = "quiz_level")
    private String quizLevel;

    @Column(name = "quiz_answertype")
    private int quizAnswerType;

    @Column(name = "quiz_explain")
    private String quizExplain;

    // FK : user_id (유저_고유번호) From : user 테이블
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    //    FK : constraint_name(제약조건 고유번호) constraintDefine 테이블
    @OneToOne
    @JoinColumn(name = "constraint_name")
    private ConstraintDefine constraintDefine;

    //getter/setter/////////////////////////////////////////////////////////////////////

    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    public String getQuizContents() {
        return quizContents;
    }

    public void setQuizContents(String quizContents) {
        this.quizContents = quizContents;
    }

    public String getQuizAnswer() {
        return quizAnswer;
    }

    public void setQuizAnswer(String quizAnswer) {
        this.quizAnswer = quizAnswer;
    }

    public int getQuizEachScore() {
        return quizEachScore;
    }

    public void setQuizEachScore(int quizEachScore) {
        this.quizEachScore = quizEachScore;
    }

    public String getQuizSubject() {
        return quizSubject;
    }

    public void setQuizSubject(String quizSubject) {
        this.quizSubject = quizSubject;
    }

    public String getQuizChapter() {
        return quizChapter;
    }

    public void setQuizChapter(String quizChapter) {
        this.quizChapter = quizChapter;
    }

    public String getQuizLevel() {
        return quizLevel;
    }

    public void setQuizLevel(String quizLevel) {
        this.quizLevel = quizLevel;
    }

    public int getQuizAnswerType() {
        return quizAnswerType;
    }

    public void setQuizAnswerType(int quizAnswerType) {
        this.quizAnswerType = quizAnswerType;
    }

    public String getQuizExplain() {
        return quizExplain;
    }

    public void setQuizExplain(String quizExplain) {
        this.quizExplain = quizExplain;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ConstraintDefine getConstraintDefine() {
        return constraintDefine;
    }

    public void setConstraintDefine(ConstraintDefine constraintDefine) {
        this.constraintDefine = constraintDefine;
    }
}
