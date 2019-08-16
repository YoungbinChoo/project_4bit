package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.ClassGroup;
import com.bitcamp.project.project_4bit.entity.ConstraintDefine;
import com.bitcamp.project.project_4bit.entity.Homework;
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
public class HomeworkRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ConstraintDefineRepository constraintDefineRepository;

    @Autowired
    private ClassGroupRepository classGroupRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreateHomework(){
        ClassGroup classGroup = classGroupRepository.findByClassId((long)1);
        User user = userRepository.findByUsername("test_t");
        ConstraintDefine constraintDefine = constraintDefineRepository.findByConstraintName("homework_constraint");

        // 새로운 빈 homework 생성
        Homework homework = new Homework();

        // 과제명
        homework.setHwName("테스트 과제");
//        // 제출기한
//        homework.setHwDeadLine();
//        // 과제 출제일
//        homework.setHwCreateDate();
//        // 과제 수정일
//        homework.setHwUpdateDate();
        // 과제 과목
        homework.setHwSubject("JAVA");
        // 과제 내용
        homework.setHwDescription("이번 과제는 없습니다");
        // 과제제출 파일첨부 유무
        homework.setHwTeachIsfile(false);

        /////////////////// FK설정
        // 반 고유번호
        homework.setClassGroup(classGroup);
        // 유저 고유번호
        homework.setUser(user);
        // 제약 이름
        homework.setConstraintDefine(constraintDefine);


        ///////////////////// 검증 파트
        Homework saved = entityManager.persist(homework);
        Assert.assertNotNull(homework);
        Assert.assertEquals(saved.getHwDescription(), homework.getHwDescription());



    }




}