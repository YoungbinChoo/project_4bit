//package com.bitcamp.project.project_4bit.repository;
//
//import com.bitcamp.project.project_4bit.entity.Article;
//import com.bitcamp.project.project_4bit.entity.File;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//
//@RunWith(SpringRunner.class)
//@DataJpaTest
//@Transactional
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//public class FileRepositoryTest {
//
//    @Autowired
//    private TestEntityManager entityManager;
//
//    @Autowired
//    private ArticleRepository articleRepository;
//
//    @Autowired
//    private FileRepository fileRepository;
//
//    @Test
//    public void testCreateArticleFile(){
//        Article article = articleRepository.findByArticleId((long)5);
//
//        File file = new File();
//        file.setFileUrl("파일주소1");
//        file.setFileName("파일명1");
//        file.setFileSize((long)50);
//
//        File saved = entityManager.persist(file);
//        Assert.assertNotNull(saved);
//        Assert.assertEquals(saved.getFileUrl(), file.getFileUrl());
//        Assert.assertEquals(saved.getFileName(), file.getFileName());
//        Assert.assertEquals(saved.getFileId(), file.getFileId());
//        Assert.assertEquals(saved.getFileSize(), file.getFileSize());
//    }
//}
