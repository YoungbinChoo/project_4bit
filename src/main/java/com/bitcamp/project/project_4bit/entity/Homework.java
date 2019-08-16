package com.bitcamp.project.project_4bit.entity;

import com.bitcamp.project.project_4bit.util.BooleanToStringConverter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "homework")
@DynamicInsert
public class Homework implements Serializable {

    // 과제 고유번호
    @Id
    @Column(columnDefinition = "BIGINT", name = "hw_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hwId;

    // 과제명
    @Column(name = "hw_name")
    private String hwName;

    // 제출기한
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "hw_deadline")
    private Date hwDeadLine;

    // 과제 출제일
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "hw_create_date")
    private Date hwCreateDate;

    // 과제 수정일
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "hw_update_date")
    private Date hwUpdateDate;

    // 과제 과목
    @Column(name = "hw_subject")
    private String hwSubject;

    // 과제 내용
    @Column(name = "hw_description")
    private String hwDescription;

    // 과제제출 파일첨부 유무
    @Column(name = "hw_teach_isfile")
    @Convert(converter = BooleanToStringConverter.class)
    private Boolean hwTeachIsfile;

    // 반 고유번호(FK)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "class_id")
    private ClassGroup classGroup;

    // 유저 고유번호(FK)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    // 제약이름(FK)
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "constraint_name")
    private ConstraintDefine constraintDefine;


    /////////////////////////////////////////////////////////////////////////////


    public Long getHwId() {
        return hwId;
    }

    public void setHwId(Long hwId) {
        this.hwId = hwId;
    }

    public String getHwName() {
        return hwName;
    }

    public void setHwName(String hwName) {
        this.hwName = hwName;
    }

    public Date getHwDeadLine() {
        return hwDeadLine;
    }

    public void setHwDeadLine(Date hwDeadLine) {
        this.hwDeadLine = hwDeadLine;
    }

    public Date getHwCreateDate() {
        return hwCreateDate;
    }

    public void setHwCreateDate(Date hwCreateDate) {
        this.hwCreateDate = hwCreateDate;
    }

    public Date getHwUpdateDate() {
        return hwUpdateDate;
    }

    public void setHwUpdateDate(Date hwUpdateDate) {
        this.hwUpdateDate = hwUpdateDate;
    }

    public String getHwSubject() {
        return hwSubject;
    }

    public void setHwSubject(String hwSubject) {
        this.hwSubject = hwSubject;
    }

    public String getHwDescription() {
        return hwDescription;
    }

    public void setHwDescription(String hwDescription) {
        this.hwDescription = hwDescription;
    }

    public Boolean getHwTeachIsfile() {
        return hwTeachIsfile;
    }

    public void setHwTeachIsfile(Boolean hwTeachIsfile) {
        this.hwTeachIsfile = hwTeachIsfile;
    }

    public ClassGroup getClassGroup() {
        return classGroup;
    }

    public void setClassGroup(ClassGroup classGroup) {
        this.classGroup = classGroup;
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