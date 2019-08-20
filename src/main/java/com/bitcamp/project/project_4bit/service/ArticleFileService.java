package com.bitcamp.project.project_4bit.service;

import com.bitcamp.project.project_4bit.entity.ArticleFile;
import com.bitcamp.project.project_4bit.repository.ArticleFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArticleFileService {

    @Autowired
    private ArticleFileRepository articleFileRepository;


    @Transactional
    public ArticleFile createArticleFile(ArticleFile articleFile){
        return articleFileRepository.save(articleFile);
    }

}
