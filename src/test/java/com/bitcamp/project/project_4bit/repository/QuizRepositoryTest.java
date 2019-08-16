package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.ConstraintDefine;
import com.bitcamp.project.project_4bit.entity.Quiz;
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
public class QuizRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConstraintDefineRepository constraintDefineRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Test
    public void CreateQuizId(){
        Quiz qu = new Quiz();
        User user = userRepository.findByUsername("test_s");
        ConstraintDefine con = constraintDefineRepository.findByConstraintName("job_constraint");

//        qu.setQuizId(1L);
        qu.setQuizContents("dd");
        qu.setQuizAnswer("string");
        qu.setQuizEachScore(10);
        qu.setQuizSubject("subject");
        qu.setQuizChapter("chapter");
        qu.setQuizLevel("상");
        qu.setQuizAnswerType(555);
        qu.setQuizExplain("팔아요");
        qu.setUser(user);
        qu.setConstraintDefine(con);

        Quiz saved = entityManager.persist(qu);
//        Quiz saved = quizRepository.save(qu);
        Assert.assertNotNull(saved);
        Assert.assertEquals(saved.getQuizId(), qu.getQuizId());
    }

    @Test //과목별로 찾기
    public void FindByQuizSubject(){
        Quiz qu = new Quiz();
        User user = userRepository.findByUsername("test_s");
        ConstraintDefine con = constraintDefineRepository.findByConstraintName("job_constraint");
  //      qu.setQuizId(1L);
        qu.setQuizContents("dd");
        qu.setQuizAnswer("string");
        qu.setQuizEachScore(10);
        qu.setQuizSubject("데이터베이스");
        qu.setQuizChapter("chapter");
        qu.setQuizLevel("상");
        qu.setQuizAnswerType(555);
        qu.setQuizExplain("팔아요");
        qu.setUser(user);
        qu.setConstraintDefine(con);

        Quiz saved = quizRepository.save(qu);
        Assert.assertNotNull(saved);
        Assert.assertEquals(saved.getQuizSubject(), "데이터베이스");
    }


    @Test //챕터별로 찾기
    public void FindByQuizChapter(){
        Quiz qu = new Quiz();
        User user = userRepository.findByUsername("test_s");
        ConstraintDefine con = constraintDefineRepository.findByConstraintName("job_constraint");
        qu.setQuizId(1L);
        qu.setQuizContents("dd");
        qu.setQuizAnswer("string");
        qu.setQuizEachScore(10);
        qu.setQuizSubject("데이터베이스");
        qu.setQuizChapter("정규화");
        qu.setQuizLevel("상");
        qu.setQuizAnswerType(555);
        qu.setQuizExplain("팔아요");
        qu.setUser(user);
        qu.setConstraintDefine(con);

        Quiz saved = quizRepository.save(qu);
        Assert.assertNotNull(saved);
        Assert.assertEquals(saved.getQuizChapter(), "정규화");
    }
    @Test //난이도별로 찾기
    public void FindByQuizLevel(){
        Quiz qu = new Quiz();
        User user = userRepository.findByUserId((long)6);
        ConstraintDefine con = constraintDefineRepository.findByConstraintName("quiz_constraint");
//        qu.setQuizId(1L);
        qu.setQuizContents("dd");
        qu.setQuizAnswer("string");
        qu.setQuizEachScore(10);
        qu.setQuizSubject("데이터베이스");
        qu.setQuizChapter("정규화");
        qu.setQuizLevel("중");
        qu.setQuizAnswerType(555);
        qu.setQuizExplain("팔아요");
        qu.setUser(user);
        qu.setConstraintDefine(con);

//        Quiz saved = quizRepository.save(qu);
        Quiz saved = entityManager.persist(qu);
        Assert.assertNotNull(saved);
        Assert.assertEquals(saved.getQuizLevel(), qu.getQuizLevel());
    }

}
