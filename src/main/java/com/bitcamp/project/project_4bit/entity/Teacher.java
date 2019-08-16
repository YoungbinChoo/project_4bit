package com.bitcamp.project.project_4bit.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "teacher")
//@DynamicInsert
public class Teacher implements Serializable {

    @Id
    @Column(columnDefinition = "BIGINT", name = "teacher_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teacherId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
