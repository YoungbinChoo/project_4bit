package com.bitcamp.project.project_4bit.entity;


import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "class_group")
@DynamicInsert
public class ClassGroup implements Serializable {

    @Id
    @Column(columnDefinition = "BIGINT", name = "class_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long classId;

    @Column(name = "class_name")
    private String className;

    @Column(name = "class_start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date classStartDate;

    @Column(name = "class_end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date classEndDate;

    @Column(name = "subject")
    private String subject;

    @OneToOne
    @JoinColumn(name = "branch_code")
    private Branch branch;

    @Transient
    private String branchCode;

    ///////////////////////////////////////////////////////////////////////////

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Date getClassStartDate() {
        return classStartDate;
    }

    public void setClassStartDate(Date classStartDate) {
        this.classStartDate = classStartDate;
    }

    public Date getClassEndDate() {
        return classEndDate;
    }

    public void setClassEndDate(Date classEndDate) {
        this.classEndDate = classEndDate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }
}
