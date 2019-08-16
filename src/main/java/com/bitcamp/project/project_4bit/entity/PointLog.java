package com.bitcamp.project.project_4bit.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "point_log")
@DynamicInsert
public class PointLog implements Serializable {

    @Id
    @Column(columnDefinition = "BIGINT", name = "point_log_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pointLogId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Column(name = "point_event_time")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date pointEventTime;

    @Column(name = "point_added")
    private int pointAdded;

    @Column(name = "point_from")
    private String pointFrom;

    public Long getPointLogId() {
        return pointLogId;
    }

    public void setPointLogId(Long pointLogId) {
        this.pointLogId = pointLogId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getPointEventTime() {
        return pointEventTime;
    }

    public void setPointEventTime(Date pointEventTime) {
        this.pointEventTime = pointEventTime;
    }

    public int getPointAdded() {
        return pointAdded;
    }

    public void setPointAdded(int pointAdded) {
        this.pointAdded = pointAdded;
    }

    public String getPointFrom() {
        return pointFrom;
    }

    public void setPointFrom(String pointFrom) {
        this.pointFrom = pointFrom;
    }
}
