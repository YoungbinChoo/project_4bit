package com.bitcamp.project.project_4bit.entity;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "student_answer")
@DynamicInsert
public class StudentAnswer implements Serializable {

    @Id
    @Column(name= "student_answer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentAnswerId;

    @Column(name="student_test_answer_content")
    private String studentTestAnswerContent;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_test_id")
    private StudentTest studentTest ;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "test_quiz_id")
    private TestQuiz testQuiz;


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public Long getStudentAnswerId() {
        return studentAnswerId;
    }

    public void setStudentAnswerId(Long studentAnswerId) {
        this.studentAnswerId = studentAnswerId;
    }

    public String getStudentTestAnswerContent() {
        return studentTestAnswerContent;
    }

    public void setStudentTestAnswerContent(String studentTestAnswerContent) {
        this.studentTestAnswerContent = studentTestAnswerContent;
    }

    public StudentTest getStudentTest() {
        return studentTest;
    }

    public void setStudentTest(StudentTest studentTest) {
        this.studentTest = studentTest;
    }

    public TestQuiz getTestQuiz() {
        return testQuiz;
    }

    public void setTestQuiz(TestQuiz testQuiz) {
        this.testQuiz = testQuiz;
    }
}
