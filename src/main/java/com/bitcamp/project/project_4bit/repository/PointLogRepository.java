package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.PointLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PointLogRepository extends JpaRepository<PointLog, Long> {


    //과제 제출 시 포인트 업데이트
    @Modifying
    @Query(value = "INSERT INTO point_log (point_added, point_from, point_event_time, user_id ) VALUE (10, '과제 제출',now(), ?1 )", nativeQuery = true)
    int updatePoinLogHomework(Long userId);

}
