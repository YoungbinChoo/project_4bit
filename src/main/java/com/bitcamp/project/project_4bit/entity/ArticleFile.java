package com.bitcamp.project.project_4bit.entity;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "article_file")
@DynamicInsert
public class ArticleFile implements Serializable {

    @Id
    @Column(name = "article_file_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleFileId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "file_id")
    private File file;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "article_id")
    private Article article;

    public Long getArticleFileId() {
        return articleFileId;
    }

    public void setArticleFileId(Long articleFileId) {
        this.articleFileId = articleFileId;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

}
