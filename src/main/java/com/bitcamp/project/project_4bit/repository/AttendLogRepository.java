package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.AttendLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;

@Repository
public interface AttendLogRepository extends JpaRepository<AttendLog, Long> {

    // student_id 로 attend_log 를 찾음
    @Query(value ="SELECT * FROM attend_log WHERE student_id = ?1 ORDER BY event_attend_time DESC LIMIT 1", nativeQuery = true)
    AttendLog findEventNameByStudentId(Long studentId);


    // attendId가 가장 큰 학생의 기록을 구해온다(가장 최근의 출석기록)
    @Query(value ="SELECT * FROM attend_log WHERE attend_log_id = (SELECT MAX(attend_log_id) FROM attend_log WHERE student_id=?1)", nativeQuery = true)
    AttendLog findByMaxAttendIdOfStudent(Long studentId);


    // 학생 ID 로 학생 출석현황 조회
    AttendLog findAllByStudent_StudentId(Long studentId);

    Page<AttendLog> findAllByStudent_StudentId(Long studentId, Pageable pageable);

}
