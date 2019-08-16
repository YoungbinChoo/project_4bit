package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.Teacher;
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
public class TeacherRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreateTeacher(){
        User user = userRepository.findByUsername("test_t");

        Teacher teacher = new Teacher();
        teacher.setUser(user);

        Teacher saved = entityManager.persist(teacher);
        Assert.assertNotNull(saved);
        Assert.assertEquals(saved.getUser(), teacher.getUser());
        Assert.assertEquals(saved.getTeacherId(), teacher.getTeacherId());



    }
}
