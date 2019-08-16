package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.Article;
import com.bitcamp.project.project_4bit.entity.ArticleFile;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ArticleFileRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleFileRepository articleFileRepository;

    @Test
    public void testCreateArticleFile(){
        Article article = articleRepository.findByArticleId((long)5);

        ArticleFile articleFile = new ArticleFile();
        articleFile.setArticle(article);
        articleFile.setFileUrl("파일주소1");
        articleFile.setFileName("파일명1");
        articleFile.setFileSize(50);

        ArticleFile saved = entityManager.persist(articleFile);
        Assert.assertNotNull(saved);
        Assert.assertEquals(saved.getFileUrl(), articleFile.getFileUrl());
        Assert.assertEquals(saved.getFileName(), articleFile.getFileName());
        Assert.assertEquals(saved.getFileId(), articleFile.getFileId());
        Assert.assertEquals(saved.getFileSize(), articleFile.getFileSize());
    }
}
