package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.Role;
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
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testCreateUser() {
        Role role = roleRepository.findByRoleName("role_student");
        User user = new User();
        user.setUsername("test_s");
        user.setPassword("1234");
        user.setName("학생테스트");
        user.setEmail("test@bitcamp.com");
        user.setRole(role);

        User saved = entityManager.persist(user);
        Assert.assertNotNull(saved);
        Assert.assertEquals(saved.getUsername(), user.getUsername());
    }

    @Test
    public void testFindByUsername() {
        User user = userRepository.findByUsername("test_s");
        Assert.assertNotNull(user);
        Assert.assertEquals(user.getUsername(), "test_s");
    }
}