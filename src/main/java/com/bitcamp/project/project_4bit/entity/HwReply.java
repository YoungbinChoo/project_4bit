package com.bitcamp.project.project_4bit.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "hw_reply")
@DynamicInsert
public class HwReply implements Serializable {

    // 과제댓글 고유번호
    @Id
    @Column(columnDefinition = "BIGINT", name = "HW_reply_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hwReplyId;

    // 과제댓글 내용
    @Column(name = "hwReplyContents")
    private String hwReplyContents;

    // 과제댓글 작성일
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "HW_reply_create_date")
    private Date hwReplyCreateDate;

    // 과제댓글 수정일
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "HW_reply_update_date")
    private Date hwReplyUpdateDate;

    // 과제제출 고유번호(FK)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "HW_article_id")
    private HwArticle hwArticle;

    // 유저 고유번호(FK)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;


    /////////////////////////////////////////////////////////////////////////////


    public Long getHwReplyId() {
        return hwReplyId;
    }

    public void setHwReplyId(Long hwReplyId) {
        this.hwReplyId = hwReplyId;
    }

    public String getHwReplyContents() {
        return hwReplyContents;
    }

    public void setHwReplyContents(String hwReplyContents) {
        this.hwReplyContents = hwReplyContents;
    }

    public Date getHwReplyCreateDate() {
        return hwReplyCreateDate;
    }

    public void setHwReplyCreateDate(Date hwReplyCreateDate) {
        this.hwReplyCreateDate = hwReplyCreateDate;
    }

    public Date getHwReplyUpdateDate() {
        return hwReplyUpdateDate;
    }

    public void setHwReplyUpdateDate(Date hwReplyUpdateDate) {
        this.hwReplyUpdateDate = hwReplyUpdateDate;
    }

    public HwArticle getHwArticle() {
        return hwArticle;
    }

    public void setHwArticle(HwArticle hwArticle) {
        this.hwArticle = hwArticle;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
