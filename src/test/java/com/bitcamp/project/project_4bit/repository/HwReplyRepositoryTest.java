package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.HwArticle;
import com.bitcamp.project.project_4bit.entity.HwReply;
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
public class HwReplyRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HwArticleRepository hwArticleRepository;

    @Test
    public void testCreateHwReply() {

        // 외부테이블 조인용 인스턴스 생성
        User user = userRepository.findByUsername("test_t");
        HwArticle hwArticle = hwArticleRepository.findByHwArticleId((long)1);

        // 빈 hwReply 생성
        HwReply hwReply = new HwReply();

        //과제댓글 내용
        hwReply.setHwReplyContents("잘 풀었습니다. 버억");
//        //과제댓글 작성일
//        hwReply.setHwReplyCreateDate();
//        //과제댓글 수정일
//        hwReply.setHwReplyUpdateDate();

        //과제제출 고유번호(FK)
        hwReply.setHwArticle(hwArticle);
        //유저 고유번호(FK)
        hwReply.setUser(user);



        ///////////////////// 검증 파트
        HwReply saved = entityManager.persist(hwReply);
        Assert.assertNotNull(hwReply);
        Assert.assertEquals(saved.getHwReplyContents(), hwReply.getHwReplyContents());

    }




}
