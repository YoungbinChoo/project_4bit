package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.Admin;
import com.bitcamp.project.project_4bit.entity.Branch;
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
public class AdminRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Test
    public void testCreateAdmin(){
        User user = userRepository.findByUsername("test_a");
        Branch branch = branchRepository.findByBranchCode("sinchon");

        Admin admin = new Admin();
        admin.setUser(user);
        admin.setBranch(branch);

        Admin saved = entityManager.persist(admin);
        Assert.assertNotNull(saved);
        Assert.assertEquals(saved.getAdminId(), admin.getAdminId());
        Assert.assertEquals(saved.getBranch(), admin.getBranch());
        Assert.assertEquals(saved.getUser(), admin.getUser());


    }


}
