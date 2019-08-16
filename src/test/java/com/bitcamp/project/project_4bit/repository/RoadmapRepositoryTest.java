package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.Roadmap;
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
public class RoadmapRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoadmapRepository roadmapRepository;

    @Test
    public void testCreateRoadmap() {
        Roadmap roadmap = new Roadmap();
        roadmap.setRoadmapStageNo(1);
        roadmap.setRoadmapSubject("JAVA");
        roadmap.setRoadmapChapter("반복문");

        Roadmap saved = entityManager.persist(roadmap);
        Assert.assertNotNull(saved);
        Assert.assertEquals(saved.getRoadmapSubject(), roadmap.getRoadmapSubject());
        Assert.assertEquals(saved.getRoadmapChapter(), roadmap.getRoadmapChapter());
    }

    @Test
    public void testFindByRoadmapStageNo() {
        Roadmap roadmap = roadmapRepository.findByRoadmapStageNo(1);
        Assert.assertNotNull(roadmap);
        Assert.assertEquals(roadmap.getRoadmapStageNo(), (Integer)1);
    }
}