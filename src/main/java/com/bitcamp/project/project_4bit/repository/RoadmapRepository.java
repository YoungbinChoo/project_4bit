package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.Roadmap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoadmapRepository extends JpaRepository<Roadmap,Integer> {

    // 해당 Roadmap 테이블에서 where 조건으로 RoadmapStageNo를 줘서 찾는것.
    Roadmap findByRoadmapStageNo(Integer roadmapStageNo);

    //  Roadmap 내용들 전체 출력
    Page<Roadmap> findAll(Pageable pageable);

    // 해당 RoadmapStageNo에 해당하는 Roadmap 하나를 조회
    @Query(value = "SELECT * FROM roadmap WHERE roadmap_stage_no=?1", nativeQuery = true)
    Roadmap findByRoadmap_RoadmapStageNo(Integer roadmapStageNo);

}
