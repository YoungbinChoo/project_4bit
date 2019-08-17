package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.StudentTest;
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

import java.util.Arrays;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StudentTestRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StudentTestRepository studentTestRepository;

    @Autowired
    private TestGroupRepository testGroupRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void CreateStudentTest(){

       TestGroup testGroup = testGroupRepository.findByTestId(1l);
       User user = userRepository.findByUsername("test_s");

       StudentTest studentTest = new StudentTest();
       studentTest.setStTestScore(40);
       studentTest.setTestGroup(testGroup);
       studentTest.setUser(user);

       StudentTest saved = entityManager.persist(studentTest);
       Assert.assertNotNull(saved);
       Assert.assertEquals(saved.getStTestScore(), studentTest.getStTestScore());
    }

    @Test
    public void StudentTestId(){
        StudentTest studentTest = studentTestRepository.findByStudentTestId(1L);
        Assert.assertNotNull(studentTest);
        Assert.assertEquals(studentTest.getStTestScore(), 100);
    }
}
