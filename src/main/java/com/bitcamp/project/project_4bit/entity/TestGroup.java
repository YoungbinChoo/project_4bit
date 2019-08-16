package com.bitcamp.project.project_4bit.entity;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "test_group")
@DynamicInsert
public class TestGroup implements Serializable {

    @Id
    @Column(name = "test_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long testId;

     @Column(name = "test_name")
    private String testName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "test_start_time")
    private Date testStartTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "test_end_time")
    private Date testEndTime;

    @Column(name = "test_description")
    private String testDescription;

    @Column(name = "sum")
    private int sum;

    @Column(name = "avg")
    private double avg;

    @Column(name = "max")
    private int max;

    @Column(name = "min")
    private int min;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "constraint_name")
    private ConstraintDefine constraintDefine;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private ClassGroup classGroup;


    /////////////////////////////////////////////////////////////////////


    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public Date getTestStartTime() {
        return testStartTime;
    }

    public void setTestStartTime(Date testStartTime) {
        this.testStartTime = testStartTime;
    }

    public Date getTestEndTime() {
        return testEndTime;
    }

    public void setTestEndTime(Date testEndTime) {
        this.testEndTime = testEndTime;
    }

    public String getTestDescription() {
        return testDescription;
    }

    public void setTestDescription(String testDescription) {
        this.testDescription = testDescription;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public double getAvg() {
        return avg;
    }

    public void setAvg(double avg) {
        this.avg = avg;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
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

    public ClassGroup getClassGroup() {
        return classGroup;
    }

    public void setClassGroup(ClassGroup classGroup) {
        this.classGroup = classGroup;
    }
}
