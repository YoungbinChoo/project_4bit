package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.Roadmap;
import com.bitcamp.project.project_4bit.entity.RoadmapExercise;
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
public class RoadmapExerciseRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoadmapRepository roadmapRepository;

    @Test
    public void testCreateRoadmapExercise() {
        Roadmap roadmap = roadmapRepository.findByRoadmapStageNo(1);
        RoadmapExercise roadmapExercise = new RoadmapExercise();
        roadmapExercise.setRoadmap(roadmap);
        roadmapExercise.setExerciseContents("문제본문");
        roadmapExercise.setExerciseAnswer("1");

        RoadmapExercise saved = entityManager.persist(roadmapExercise);
        Assert.assertNotNull(saved);
        Assert.assertEquals(saved.getExerciseContents(), roadmapExercise.getExerciseContents());
        Assert.assertEquals(saved.getExerciseAnswer(), roadmapExercise.getExerciseAnswer());
    }

}