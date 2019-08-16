package com.bitcamp.project.project_4bit.entity;


import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "student")
@DynamicInsert
public class Student implements Serializable {

    // 학생 고유번호
    @Id
    @Column(columnDefinition = "BIGINT", name = "student_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;

    // 생년월일
    @Column(name = "student_birth")
    private String studentBirth;

    // 로드맵 마지막 단계
    @Column(name = "roadmap_last")
    private int roadmapLast;

    // 유저 고유번호(FK)
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

//    // 반 고유번호(FK)
//    @OneToMany(fetch = FetchType.EAGER)
//    @JoinColumn(name = "class_id")
//    private ClassGroup classGroup;

    // 반 고유번호(FK)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "class_id")
    private ClassGroup classGroup;

    // 출석횟수
    @Column(name = "attend_count")
    private int attendCount;

    // 결석횟수
    @Column(name = "absent_count")
    private int absentCount;

    // 지각횟수
    @Column(name = "late_count")
    private int lateCount;

    // 외출횟수
    @Column(name = "out_count")
    private int outCount;

    // 조퇴횟수
    @Column(name = "early_leave_count")
    private int earlyLeaveCount;

    // 상담내용
    @Column(name = "counsel")
    private String counsel;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentBirth() {
        return studentBirth;
    }

    public void setStudentBirth(String studentBirth) {
        this.studentBirth = studentBirth;
    }

    public int getRoadmapLast() {
        return roadmapLast;
    }

    public void setRoadmapLast(int roadmapLast) {
        this.roadmapLast = roadmapLast;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ClassGroup getClassGroup() {
        return classGroup;
    }

    public void setClassGroup(ClassGroup classGroup) {
        this.classGroup = classGroup;
    }

    public int getAttendCount() {
        return attendCount;
    }

    public void setAttendCount(int attendCount) {
        this.attendCount = attendCount;
    }

    public int getAbsentCount() {
        return absentCount;
    }

    public void setAbsentCount(int absentCount) {
        this.absentCount = absentCount;
    }

    public int getLateCount() {
        return lateCount;
    }

    public void setLateCount(int lateCount) {
        this.lateCount = lateCount;
    }

    public int getOutCount() {
        return outCount;
    }

    public void setOutCount(int outCount) {
        this.outCount = outCount;
    }

    public int getEarlyLeaveCount() {
        return earlyLeaveCount;
    }

    public void setEarlyLeaveCount(int earlyLeaveCount) {
        this.earlyLeaveCount = earlyLeaveCount;
    }

    public String getCounsel() {
        return counsel;
    }

    public void setCounsel(String counsel) {
        this.counsel = counsel;
    }
}