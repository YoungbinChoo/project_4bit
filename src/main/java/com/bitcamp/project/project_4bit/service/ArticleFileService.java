package com.bitcamp.project.project_4bit.service;

import com.bitcamp.project.project_4bit.entity.ArticleFile;
import com.bitcamp.project.project_4bit.entity.File;
import com.bitcamp.project.project_4bit.repository.ArticleFileRepository;
import com.bitcamp.project.project_4bit.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ArticleFileService {

    @Autowired
    private ArticleFileRepository articleFileRepository;

    @Transactional
    public ArticleFile createArticleFile(ArticleFile articleFile){
        return articleFileRepository.save(articleFile);
    }

//    @Transactional
//    public ArticleFile findArticleFile(Long articleId){
//        return articleFileRepository.findByArticleFileId(articleId);
//    }

    @Transactional
    public List<ArticleFile> findArticleFile(Long articleId){
        return articleFileRepository.findByArticle_ArticleId(articleId);
    }

}
