package com.bitcamp.project.project_4bit.entity;

import com.bitcamp.project.project_4bit.model.StAnswer;
import com.bitcamp.project.project_4bit.util.JsonCollectionToStringConverter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "student_test")
@DynamicInsert
public class StudentTest implements Serializable {

    @Id
    @Column(name = "student_test_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentTestId;

    @Column(name = "st_answer")
    @Convert(converter = JsonCollectionToStringConverter.class)
    private Collection<StAnswer> stAnswer;

    @Column(name = "st_test_score")
    private int stTestScore;

	@ManyToOne
    @JoinColumn(name = "test_id")
    private TestGroup testGroup;
	
	@ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

	///////////////////////////////////////////////////////////////////////////

    public Long getStudentTestId() {
        return studentTestId;
    }

    public void setStudentTestId(Long studentTestId) {
        this.studentTestId = studentTestId;
    }

    public Collection<StAnswer> getStAnswer() {
        return stAnswer;
    }

    public void setStAnswer(Collection<StAnswer> stAnswer) {
        this.stAnswer = stAnswer;
    }

    public int getStTestScore() {
        return stTestScore;
    }

    public void setStTestScore(int stTestScore) {
        this.stTestScore = stTestScore;
    }

    public TestGroup getTestGroup() {
        return testGroup;
    }

    public void setTestGroup(TestGroup testGroup) {
        this.testGroup = testGroup;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
