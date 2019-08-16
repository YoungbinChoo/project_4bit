package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.ConstraintDefine;
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
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class ConstraintDefineTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ConstraintDefineRepository ConstraintDefineRepository;

    @Test
    public void testCreateConstraintDefine(){
        ConstraintDefine constraintDefine = new ConstraintDefine();
        constraintDefine.setTitleLength(500);
        constraintDefine.setContentLength(500);
        constraintDefine.setEachFileSizeLimit(500);
        constraintDefine.setFileCount(500);
        constraintDefine.setTotalFileSizeLimit(500);
        constraintDefine.setReplyLength(500);
        constraintDefine.setArticleDepthLimit(500);
        constraintDefine.setReplyDepthLimit(500);
    }
}
