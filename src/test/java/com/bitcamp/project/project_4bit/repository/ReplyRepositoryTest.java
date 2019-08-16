package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.Article;
import com.bitcamp.project.project_4bit.entity.Reply;
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
@DataJpaTest            // @DataJpaTest 를 주면 entityManager를 사용하여 인메모리 테스트를 할 수 있다. 실제로 DB에 저장하지 않고 인메모리에 저장된 것을 검증 가능
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 실제 DB에 있는 내용을 테스트 할 수 있다. findByBranchCode 같은 실제 DB에 있는 내용을 검증 가능
public class ReplyRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    ReplyRepository replyRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ArticleRepository articleRepository;

    @Test
    public void testCreateReply(){
        User user = userRepository.findByUsername("test_s");
        Article article = articleRepository.findByArticleId((long)5);
        Reply reply = new Reply();
        reply.setUser(user);
        reply.setReplyContents("스티브쌤 항상 감사합니다!");
        reply.setArticle(article);

        Reply saved = entityManager.persist(reply);
        Assert.assertNotNull(saved);
        Assert.assertEquals(saved.getReplyContents(), reply.getReplyContents());

    }

}
