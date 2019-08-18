package com.bitcamp.project.project_4bit.entity;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "article_file")
@DynamicInsert
public class ArticleFile implements Serializable {

    // PK : article_file_id
    @Id
    @Column(columnDefinition = "BIGINT", name = "article_file_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleFileId;

    // FK : 게시물_고유번호 article_id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "article_id")
    private Article article;

    // FK : 파일_고유번호 file_id
    @ManyToOne(fetch = FetchType.EAGER)
    private File file;




    public Long getArticleFileId() {
        return articleFileId;
    }

    public void setArticleFileId(Long articleFileId) {
        this.articleFileId = articleFileId;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
