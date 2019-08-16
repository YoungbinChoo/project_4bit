package com.bitcamp.project.project_4bit.entity;

import com.bitcamp.project.project_4bit.util.BooleanToStringConverter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "hw_article")
@DynamicInsert
public class HwArticle implements Serializable {

    // 과제제출_고유번호
    @Id
    @Column(columnDefinition = "BIGINT", name = "HW_article_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hwArticleId;

    // 제출날짜
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "hw_submit_date")
    private Date hwSubmitDate;

    // 수정날짜
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "hw_update_date")
    private Date hwUpdateDate;

    // 과제내용
    @Column(name = "hw_contents")
    private String hwContents;

    // 과제 파일첨부 유무
    @Column(name = "hw_isfile")
    @Convert(converter = BooleanToStringConverter.class)
    private Boolean hwIsFile;

    // 과제 고유번호(FK)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hw_id")
    private Homework homework;

    // 유저 고유번호(FK)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;


    /////////////////////////////////////////////////////////////////////////////


    public Long getHwArticleId() {
        return hwArticleId;
    }

    public void setHwArticleId(Long hwArticleId) {
        this.hwArticleId = hwArticleId;
    }

    public Date getHwSubmitDate() {
        return hwSubmitDate;
    }

    public void setHwSubmitDate(Date hwSubmitDate) {
        this.hwSubmitDate = hwSubmitDate;
    }

    public Date getHwUpdateDate() {
        return hwUpdateDate;
    }

    public void setHwUpdateDate(Date hwUpdateDate) {
        this.hwUpdateDate = hwUpdateDate;
    }

    public String getHwContents() {
        return hwContents;
    }

    public void setHwContents(String hwContents) {
        this.hwContents = hwContents;
    }

    public Boolean getHwIsFile() {
        return hwIsFile;
    }

    public void setHwIsFile(Boolean hwIsFile) {
        this.hwIsFile = hwIsFile;
    }

    public Homework getHomework() {
        return homework;
    }

    public void setHomework(Homework homework) {
        this.homework = homework;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}