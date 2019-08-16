package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.Homework;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface HomeworkRepository extends JpaRepository<Homework, Long> {
    Homework findByHwId(Long hwId);

    // 반환형이 int인데 0이면 업데이트 실패, 1이면 성공이라고 함
    @Modifying
    @Query(value = "UPDATE homework h SET h.hw_name=?2, h.hw_deadline=?3, h.hw_subject=?4, h.hw_description=?5, h.hw_update_date=now() WHERE h.hw_id =?1", nativeQuery = true)
    int updateHomework(Long hwId, String newHwName, Date newHwDeadLine, String newHwSubject, String newHwDescription);

    // 현재 진행중인 과제만(hw_deadline > 요청된 시간) 찾기
    @Query(value = "SELECT * FROM homework WHERE class_id=?1 AND hw_deadline > ?2", nativeQuery = true)
    Page<Homework> findAllByClassIdAndRequestedTime(Long classId, String requestedTime, Pageable pageable);
}