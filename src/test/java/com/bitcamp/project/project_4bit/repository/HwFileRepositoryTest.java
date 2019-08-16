package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.HwArticle;
import com.bitcamp.project.project_4bit.entity.HwFile;
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
public class HwFileRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private HwArticleRepository hwArticleRepository;

    @Test
    public void testCreateHwFile() {

        // 외부테이블 조인용 인스턴스 생성
        HwArticle hwArticle = hwArticleRepository.findByHwArticleId((long)1);

        // 빈 hwFile생성
        HwFile hwFile = new HwFile();

        // 과제파일 주소
        hwFile.setHwFileUrl("http://www.naver.com");
        // 과제파일명
        hwFile.setHwFileName("test.zip");
        // 과제파일 크기
        hwFile.setHwFileSize(1024);
        // 과제제출 고유번호(FK)
        hwFile.setHwArticle(hwArticle);

        ///////////////////// 검증 파트
        HwFile saved = entityManager.persist(hwFile);
        Assert.assertNotNull(hwFile);
        Assert.assertEquals(saved.getHwFileUrl(), hwFile.getHwFileUrl());

    }
}
