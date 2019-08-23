package com.bitcamp.project.project_4bit.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "class_teacher_log")
@DynamicInsert
public class ClassTeacherLog implements Serializable {

    @Id
    @Column(columnDefinition = "BIGINT", name = "class_teacher_log_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long classTeacherId;

    @OneToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @OneToOne
    @JoinColumn(name = "class_id")
    private ClassGroup classGroup;

    @Column(name = "class_teacher_event_time")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date classTeacherEventTime;

    @Column(name = "class_teacher_description")
    private String classTeacherDescription;

    public Long getClassTeacherId() {
        return classTeacherId;
    }

    public void setClassTeacherId(Long classTeacherId) {
        this.classTeacherId = classTeacherId;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public ClassGroup getClassGroup() {
        return classGroup;
    }

    public void setClassGroup(ClassGroup classGroup) {
        this.classGroup = classGroup;
    }

    public Date getClassTeacherEventTime() {
        return classTeacherEventTime;
    }

    public void setClassTeacherEventTime(Date classTeacherEventTime) {
        this.classTeacherEventTime = classTeacherEventTime;
    }

    public String getClassTeacherDescription() {
        return classTeacherDescription;
    }

    public void setClassTeacherDescription(String classTeacherDescription) {
        this.classTeacherDescription = classTeacherDescription;
    }
}
