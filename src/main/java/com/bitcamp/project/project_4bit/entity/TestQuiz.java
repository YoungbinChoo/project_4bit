package com.bitcamp.project.project_4bit.entity;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "test_quiz")
@DynamicInsert
public class TestQuiz implements Serializable {

    @Id
    @Column(name = "test_quiz_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long testQuizId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "test_id")
    private TestGroup testGroup;

    @Column(name = "test_quiz_no")
    private int testQuizNo;

    //getter/setter/////////////////////////////////////////////////////////////////////

    public Long getTestQuizId() {
        return testQuizId;
    }

    public void setTestQuizId(Long testQuizId) {
        this.testQuizId = testQuizId;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public TestGroup getTestGroup() {
        return testGroup;
    }

    public void setTestGroup(TestGroup testGroup) {
        this.testGroup = testGroup;
    }

    public int getTestQuizNo() {
        return testQuizNo;
    }

    public void setTestQuizNo(int testQuizNo) {
        this.testQuizNo = testQuizNo;
    }
}
