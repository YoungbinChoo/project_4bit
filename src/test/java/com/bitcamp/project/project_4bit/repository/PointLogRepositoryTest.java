package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.PointLog;
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
public class PointLogRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreatePointLog(){
        User user = userRepository.findByUsername("test_s");

        PointLog pointLog = new PointLog();
        pointLog.setUser(user);
        pointLog.setPointAdded(10);
        pointLog.setPointFrom("quiz");

        String pointEventTime = "2020-01-08 11:00:00";
        pointLog.setPointEventTime(java.sql.Timestamp.valueOf(pointEventTime));

        PointLog saved = entityManager.persist(pointLog);
        Assert.assertNotNull(saved);
        Assert.assertEquals(saved.getUser(), pointLog.getUser());
        Assert.assertEquals(saved.getPointAdded(), pointLog.getPointAdded());
        Assert.assertEquals(saved.getPointFrom(), pointLog.getPointFrom());
        Assert.assertEquals(saved.getPointEventTime(), pointLog.getPointEventTime());

    }
}
