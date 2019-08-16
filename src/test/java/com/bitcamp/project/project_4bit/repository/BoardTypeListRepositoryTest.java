package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.BoardTypeList;
import com.bitcamp.project.project_4bit.entity.ClassGroup;
import com.bitcamp.project.project_4bit.entity.ConstraintDefine;
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
public class BoardTypeListRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ConstraintDefineRepository constraintDefineRepository;

    @Autowired
    private ClassGroupRepository classGroupRepository;

    @Test
    public void testCreateBoardManager(){
        ClassGroup classGroup = classGroupRepository.findByClassId((long)1);
        ConstraintDefine constraintDefine = constraintDefineRepository.findByConstraintName("class_board_constraint");

        BoardTypeList boardTypeList = new BoardTypeList();
        boardTypeList.setBoardId("class_1_board");
        boardTypeList.setBoardName("1반 자유게시판");
        boardTypeList.setClassGroup(classGroup);
        boardTypeList.setConstraintDefine(constraintDefine);

        BoardTypeList saved = entityManager.persist(boardTypeList);
        Assert.assertNotNull(saved);
        Assert.assertEquals(saved.getBoardId(), boardTypeList.getBoardId());
        Assert.assertEquals(saved.getBoardName(), boardTypeList.getBoardName());
        Assert.assertEquals(saved.getConstraintDefine(), boardTypeList.getConstraintDefine());

    }
}
