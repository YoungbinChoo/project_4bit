package com.bitcamp.project.project_4bit.entity;

import com.bitcamp.project.project_4bit.util.BolleanToInoutConverter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "attend_log")
@DynamicInsert
public class AttendLog implements Serializable {

    @Id
    @Column(columnDefinition = "BIGINT", name = "attend_log_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendLogId;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "event_attend_time", updatable = false, nullable = false)
    private Date eventAttendTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(name = "event_name")
//    @Convert(converter =  BolleanToInoutConverter.class)
    private String eventName;

    public Long getAttendLogId() {
        return attendLogId;
    }

    public void setAttendLogId(Long attendLogId) {
        this.attendLogId = attendLogId;
    }

    public Date getEventAttendTime() {
        return eventAttendTime;
    }

    public void setEventAttendTime(Date eventAttendTime) {
        this.eventAttendTime = eventAttendTime;
    }

//    public Boolean getEventName() {
//        return eventName;
//    }
//
//    public void setEventName(Boolean eventName) {
//        this.eventName = eventName;
//    }


    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
