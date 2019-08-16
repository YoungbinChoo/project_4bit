package com.bitcamp.project.project_4bit.entity;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;

//게시물 파일 테이블
@Entity
@Table(name = "article_file")
@DynamicInsert
public class ArticleFile implements Serializable {

    // PK : file_id 파일_고유번호     AutoIncrement를 사용
    @Id
    @Column(columnDefinition = "BIGINT", name = "file_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    // file_url 파일주소
    @Column(name = "file_url")
    private String fileUrl;

    // FK : article_id  게시물_고유번호(from : article)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "article_id")
    private Article article;

    // file_name 파일명
    @Column(name = "file_name")
    private String fileName;

    // file_size 파일크기기
    @Column(name = "file_size")
    private Integer fileSize;       // int 형으로 해야될지 Integer로 해야될지 고민

    //////////////////////////////////////////////////////////////////////////////////////////////


    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }
}
