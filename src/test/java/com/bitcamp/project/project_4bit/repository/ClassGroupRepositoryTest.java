package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.Branch;
import com.bitcamp.project.project_4bit.entity.ClassGroup;
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
public class ClassGroupRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BranchRepository branchRepository;

    @Test
    public void testCreateClassGroup(){
        Branch branch = branchRepository.findByBranchCode("sinchon");

        ClassGroup classGroup = new ClassGroup();
        classGroup.setBranch(branch);
        classGroup.setClassName("자바 개발자 양성과정");
        classGroup.setSubject("Java");

        String classStartDate = "2019-08-08 11:00:00";
        String classEndDate = "2020-01-08 11:00:00";
        classGroup.setClassStartDate(java.sql.Timestamp.valueOf(classStartDate));
        classGroup.setClassEndDate(java.sql.Timestamp.valueOf(classEndDate));


        ClassGroup saved = entityManager.persist(classGroup);
        Assert.assertNotNull(saved);
        Assert.assertEquals(saved.getClassName(), classGroup.getClassName());
        Assert.assertEquals(saved.getSubject(), classGroup.getSubject());
        Assert.assertEquals(saved.getClassStartDate(), classGroup.getClassStartDate());
        Assert.assertEquals(saved.getClassEndDate(), classGroup.getClassEndDate());


    }
}
