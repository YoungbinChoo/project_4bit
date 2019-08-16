package com.bitcamp.project.project_4bit.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

// reply 테이블
@Entity
@Table(name = "reply")
@DynamicInsert
public class Reply implements Serializable {

    // PK : reply_id (댓글_고유번호)
    @Id
    @Column(columnDefinition = "BIGINT", name = "reply_id",updatable = false,nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyId;

    //reply_contents (댓글_내용)
    @Column(name = "reply_contents")
    private String replyContents;

    //reply_create_date(댓글_작성일)
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "reply_create_date")
    private Date replyCreateDate;

    //reply_update_date (댓글_수정일)
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "reply_update_date")
    private Date replyUpdateDate;

    //reply_group_id (댓글_그룹ID)
    @Column(name = "reply_group_id")
    private int replyGroupId;

    //reply_depth (댓글_뎁쓰)
    @Column(name = "reply_depth")
    private int replyDepth;

    //reply_sequence (댓글_시퀀스)
    @Column(name = "reply_sequence")
    private int replySequence;

    // FK : article_id (게시물_고유번호)  From : article 테이블
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "article_id")
    private Article article;

    // FK : user_id (유저_고유번호) From : user 테이블
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;


///////////////////////////////////////////////////////////////////////////////////////////////////////

    public Long getReplyId() {
        return replyId;
    }

    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }

    public String getReplyContents() {
        return replyContents;
    }

    public void setReplyContents(String replyContents) {
        this.replyContents = replyContents;
    }

    public Date getReplyCreateDate() {
        return replyCreateDate;
    }

    public void setReplyCreateDate(Date replyCreateDate) {
        this.replyCreateDate = replyCreateDate;
    }

    public Date getReplyUpdateDate() {
        return replyUpdateDate;
    }

    public void setReplyUpdateDate(Date replyUpdateDate) {
        this.replyUpdateDate = replyUpdateDate;
    }

    public int getReplyGroupId() {
        return replyGroupId;
    }

    public void setReplyGroupId(int replyGroupId) {
        this.replyGroupId = replyGroupId;
    }

    public int getReplyDepth() {
        return replyDepth;
    }

    public void setReplyDepth(int replyDepth) {
        this.replyDepth = replyDepth;
    }

    public int getReplySequence() {
        return replySequence;
    }

    public void setReplySequence(int replySequence) {
        this.replySequence = replySequence;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}