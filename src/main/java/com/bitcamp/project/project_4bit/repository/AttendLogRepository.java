package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.AttendLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendLogRepository extends JpaRepository<AttendLog, Long> {


//
    @Query(value ="SELECT * FROM attend_log WHERE student_id = ?1 ORDER BY event_attend_time DESC LIMIT 1", nativeQuery = true)
    AttendLog findEventNameByStudentId(Long studentId);
}
