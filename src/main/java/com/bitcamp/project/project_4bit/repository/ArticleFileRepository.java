package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.ArticleFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleFileRepository extends JpaRepository<ArticleFile, Long> {
    // articleId 로 article_file 의 정보를 찾음
    List<ArticleFile> findByArticle_ArticleId(Long articleId);


}
