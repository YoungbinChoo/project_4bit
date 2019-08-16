package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.RoadmapExercise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoadmapExerciseRepository extends JpaRepository<RoadmapExercise,Long> {

    // 해당 ExerciseSequence에 대한 모든 RoadmapExercise을 출력
    Page<RoadmapExercise> findAll(Pageable pageable);

    // 해당 roadmap_stage_no에 해당하는 exerciseSequence 하나를 조회
    @Query(value = "SELECT * \n" +
            "FROM roadmap_exercise\n" +
            "JOIN roadmap USING (roadmap_stage_no)\n" +
            "WHERE roadmap_stage_no=?1", nativeQuery = true)
    Long findByRoadmapExercise_ExerciseSequence(Long exerciseSequence);
}
