package com.bitcamp.project.project_4bit.entity;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

// 게시판_제약사항 테이블
@Entity
@Table(name = "constraint_define")
@DynamicInsert
public class ConstraintDefine implements Serializable {

    // PK : constraint_name  (제약명)
    @Id
    @Column(columnDefinition = "VARCHAR(50)", name = "constraint_name", updatable = false, nullable = false)
    private String constraintName;

    // 제목글자수 (title_length)
    @Column(name = "title_length")
    private int titleLength;

    // 내용글자수 (content_length)
    @Column(name = "content_length")
    private int contentLength;

    // 개당첨부파일크기 (each_file_size_limit)
    @Column(name = "each_file_size_limit")
    private int eachFileSizeLimit;

    // 파일첨부개수 (file_count)
    @Column(name = "file_count")
    private int fileCount;

    // 총파일크기 (total_file_size_limit)
    @Column(name = "total_file_size_limit")
    private int totalFileSizeLimit;

    // 댓글글자수 (reply_length)
    @Column(name = "reply_length")
    private int replyLength;

    // 게시물뎁쓰제한 (article_depth_limit)
    @Column(name = "article_depth_limit")
    private int articleDepthLimit;

    // 댓글뎁쓰제한 (reply_depth_limit)
    @Column(name = "reply_depth_limit")
    private int replyDepthLimit;

    //////////////////////////////////////////////////////////


    public String getConstraintName() {
        return constraintName;
    }

    public void setConstraintName(String constraintName) {
        this.constraintName = constraintName;
    }

    public int getTitleLength() {
        return titleLength;
    }

    public void setTitleLength(int titleLength) {
        this.titleLength = titleLength;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public int getEachFileSizeLimit() {
        return eachFileSizeLimit;
    }

    public void setEachFileSizeLimit(int eachFileSizeLimit) {
        this.eachFileSizeLimit = eachFileSizeLimit;
    }

    public int getFileCount() {
        return fileCount;
    }

    public void setFileCount(int fileCount) {
        this.fileCount = fileCount;
    }

    public int getTotalFileSizeLimit() {
        return totalFileSizeLimit;
    }

    public void setTotalFileSizeLimit(int totalFileSizeLimit) {
        this.totalFileSizeLimit = totalFileSizeLimit;
    }

    public int getReplyLength() {
        return replyLength;
    }

    public void setReplyLength(int replyLength) {
        this.replyLength = replyLength;
    }

    public int getArticleDepthLimit() {
        return articleDepthLimit;
    }

    public void setArticleDepthLimit(int articleDepthLimit) {
        this.articleDepthLimit = articleDepthLimit;
    }

    public int getReplyDepthLimit() {
        return replyDepthLimit;
    }

    public void setReplyDepthLimit(int replyDepthLimit) {
        this.replyDepthLimit = replyDepthLimit;
    }
}
