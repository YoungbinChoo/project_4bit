package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.PointLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointLogRepository extends JpaRepository<PointLog, Long> {

}
