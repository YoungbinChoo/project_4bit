package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.ClassGroup;
import com.bitcamp.project.project_4bit.entity.ClassTeacherLog;
import com.bitcamp.project.project_4bit.entity.Teacher;
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
public class ClassTeacherLogRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private ClassGroupRepository classGroupRepository;

    @Test
    public void testCreateClassTeacherLog(){
        Teacher teacher = teacherRepository.findByTeacherId((long)1);
        ClassGroup classGroup = classGroupRepository.findByClassId((long)1);

        ClassTeacherLog classTeacherLog = new ClassTeacherLog();
        classTeacherLog.setClassTeacherDescription("스티브썜은 열정적이시다");
        classTeacherLog.setClassGroup(classGroup);
        classTeacherLog.setTeacher(teacher);

        String eventTime = "2019-08-08 11:00:00";
        classTeacherLog.setClassTeacherEventTime(java.sql.Timestamp.valueOf(eventTime));

        ClassTeacherLog saved = entityManager.persist(classTeacherLog);
        Assert.assertNotNull(saved);
        Assert.assertEquals(saved.getClassTeacherDescription(), classTeacherLog.getClassTeacherDescription());
        Assert.assertEquals(saved.getClassTeacherEventTime(), classTeacherLog.getClassTeacherEventTime());


    }

}
