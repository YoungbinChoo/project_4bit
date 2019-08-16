package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.Article;
import com.bitcamp.project.project_4bit.entity.BoardTypeList;
import com.bitcamp.project.project_4bit.entity.User;
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
public class ArticleRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardTypeListRepository boardTypeListRepository;


    @Test
    public void testCreateArticle() {
        User user = userRepository.findByUsername("test_s");
        BoardTypeList boardTypeList = boardTypeListRepository.findByBoardId("class_1_board");

        Article article = new Article();
        article.setUser(user);
        article.setArticleContents("Item #1");
        article.setBoardTypeList(boardTypeList);
//        todo.setCompleted(false);

//        Article saved = articleRepository.save(article);
//        Assert.assertNotNull(saved);
//        Assert.assertEquals(saved.getArticleContents(), article.getArticleContents());

        Article saved = entityManager.persist(article);
        Assert.assertNotNull(saved);
        Assert.assertEquals(saved.getArticleContents(), article.getArticleContents());

        // 질문 : 엔티티매니저를 왜 쓰는 건지? 아티클레파지토리를 안쓰고있는데 그러면 레파지토리에 대한 검증을 안한거 아닌가?
    }
}