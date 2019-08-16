package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.Attend;
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
public class AttendRepositoryTest {
    @Autowired
    private TestEntityManager entityManager; //실제로 디비에 저장하지 않고 인메모리에 저장된 것을 검증가능

    @Autowired
    private AttendRepository attendRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreateInsert(){
        User user = userRepository.findByUsername("test_s");

        Attend attend = new Attend();
        attend.setUser(user);
        attend.setAttendCount(1);
        attend.setAbsentCount(1);
        attend.setLateCount(1);
        attend.setOutCount(1);
        attend.setEarlyLeaveCount(1);

        Attend saved = entityManager.persist(attend);
        Assert.assertNotNull(saved);
        Assert.assertEquals(saved.getAttendCount(),1);
    }
}