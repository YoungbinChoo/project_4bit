package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.AttendLog;
import com.bitcamp.project.project_4bit.entity.Student;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class AttendLogRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AttendLogRepository attendLogRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Test
        public void testCreateAttendLog(){
//        User user = studentRepository.findByUsername("test_2");
//        long userId = student.getStudentId(); //유저id받아오기
        Student student = studentRepository.findByStudentId((long) 1) ;

//        Long.valueOf(3L)

        Date today = new Date(); //오늘날짜 체크

        AttendLog attendLog = new AttendLog();
        attendLog.setEventAttendTime(today);
        attendLog.setEventName("IN");
        attendLog.setStudent(student);

        AttendLog saved = entityManager.persist(attendLog);
        Assert.assertNotNull(saved);
        Assert.assertEquals(saved.getEventAttendTime(),attendLog.getEventAttendTime());
    }
}
