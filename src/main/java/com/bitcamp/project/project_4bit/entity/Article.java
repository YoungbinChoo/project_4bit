package com.bitcamp.project.project_4bit.entity;

import com.bitcamp.project.project_4bit.util.BooleanToStringConverter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

// article 테이블
@Entity
@Table(name = "article")
@DynamicInsert
public class Article implements Serializable {

    // PK : article_id (게시물_고유번호)
    @Id
    @Column(columnDefinition = "BIGINT", name = "article_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleId;

    //article_number (게시물번호)
    @Column(name = "article_number")
    private int articleNumber;

    //article_create_date (작성일)
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "article_create_date")
    private Date articleCreateDate;

    //article_update_date(수정일)
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "article_update_date")
    private Date articleUpdateDate;

    //article_hits (조회수)
    @Column(name = "article_hits")
    private int articleHits;


    //article_like (좋아요)
    @Column(name = "article_like")
    private int articleLike;

    //group_id (그룹ID)
    @Column(name = "group_id")
    private int groupId;

    //depth (뎁쓰)
    @Column(name = "depth")
    private int depth;

    //sequence (시퀀스)
    @Column(name = "sequence")
    private int sequence;

    //article_title (게시물제목)
    @Column(name = "article_title")
    private String articleTitle;

    //article_contents (게시물내용)
    @Column(name = "article_contents")
    private String articleContents;


    // FK : board_id (게시판영문명) From : board_manager 테이블
    @OneToOne
    @JoinColumn(name = "board_id")
    private BoardTypeList boardTypeList;

    // FK : user_id (유저_고유번호) From : user 테이블
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;



    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public int getArticleNumber() {
        return articleNumber;
    }

    public void setArticleNumber(int articleNumber) {
        this.articleNumber = articleNumber;
    }

    public Date getArticleCreateDate() {
        return articleCreateDate;
    }

    public void setArticleCreateDate(Date articleCreateDate) {
        this.articleCreateDate = articleCreateDate;
    }

    public Date getArticleUpdateDate() {
        return articleUpdateDate;
    }

    public void setArticleUpdateDate(Date articleUpdateDate) {
        this.articleUpdateDate = articleUpdateDate;
    }

    public int getArticleHits() {
        return articleHits;
    }

    public void setArticleHits(int articleHits) {
        this.articleHits = articleHits;
    }

    public int getArticleLike() {
        return articleLike;
    }

    public void setArticleLike(int articleLike) {
        this.articleLike = articleLike;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getArticleContents() {
        return articleContents;
    }

    public void setArticleContents(String articleContents) {
        this.articleContents = articleContents;
    }

    public BoardTypeList getBoardTypeList() {
        return boardTypeList;
    }

    public void setBoardTypeList(BoardTypeList boardTypeList) {
        this.boardTypeList = boardTypeList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
