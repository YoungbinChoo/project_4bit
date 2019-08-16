package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.Homework;
import com.bitcamp.project.project_4bit.entity.HwArticle;
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
public class HwArticleRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HomeworkRepository homeworkRepository;

    @Test
    public void testCreateHwArticle() {

        // 외부테이블 조인용 인스턴스 생성(?)
        User user = userRepository.findByUsername("test_s");
        Homework homework = homeworkRepository.findByHwId((long)1);

        // 빈 hwArticle 생성
        HwArticle hwArticle = new HwArticle();

//        // 제출 날짜
//        hwArticle.setHwSubmitDate();
//        // 수정 날짜
//        hwArticle.setHwUpdateDate();
        // 과제내용
        hwArticle.setHwContents("과제 답안 #1 제출 합니다");
        // 과제 파일첨부 유무
        hwArticle.setHwIsFile(false);

        ///////////////////// FK설정
        hwArticle.setHomework(homework);
        hwArticle.setUser(user);


        ///////////////////// 검증 파트
        HwArticle saved = entityManager.persist(hwArticle);
        Assert.assertNotNull(hwArticle);
        Assert.assertEquals(saved.getHwContents(), hwArticle.getHwContents());

    }

}
