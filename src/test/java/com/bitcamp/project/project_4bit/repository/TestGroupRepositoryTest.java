package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.ConstraintDefine;
import com.bitcamp.project.project_4bit.entity.TestGroup;
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
public class TestGroupRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TestGroupRepository testGroupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConstraintDefineRepository constraintDefineRepository;


    @Test
    public void CreateTestGroup() {

        User user = userRepository.findByUsername("test_s");
        ConstraintDefine constraintDefine = constraintDefineRepository.findByConstraintName("test_constraint");

        TestGroup testGroup = new TestGroup();
        testGroup.setTestName("테스트");
        testGroup.setSum(50);
        testGroup.setAvg(35);
        testGroup.setUser(user);
        testGroup.setConstraintDefine(constraintDefine);

        TestGroup saved = entityManager.persist(testGroup);
        Assert.assertNotNull(saved);
        Assert.assertEquals(saved.getSum(), testGroup.getSum());
    }


    @Test
    public void findByTestId(){
        TestGroup testGroup = testGroupRepository.findByTestId(1L);
        Assert.assertNotNull(testGroup);
        Assert.assertEquals(testGroup.getTestName(), "자바클래스");
    }
}
