package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.AttendLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AttendLogRepository extends JpaRepository<AttendLog, Long> {

    // student_id 로 attend_log 를 찾음
    @Query(value ="SELECT * FROM attend_log WHERE student_id = ?1 ORDER BY event_attend_time DESC LIMIT 1", nativeQuery = true)
    AttendLog findEventNameByStudentId(Long studentId);

    // 학생 ID 로 학생 출석로그 찾기
    AttendLog findByStudent_StudentId(Long studentId);

    // 학생의 출석현황 list 뽑기
    Page<AttendLog> findAllByStudent_StudentId(Long studentId, Pageable pageable);
    Page<AttendLog> findAllByStudent_User_userId(Long studentId, Pageable pageable);

    // 모든 count 를 0으로 바꾸기위한 쿼리
    @Modifying
    @Query(value = "UPDATE attend_log SET daily_attend_count =0",nativeQuery = true)
    int updateDailyAttendCount();

    // attendId가 가장 큰 학생의 기록을 구해온다(가장 최근의 출석기록)
    @Query(value ="SELECT * FROM attend_log WHERE attend_log_id = (SELECT MAX(attend_log_id) FROM attend_log WHERE student_id=?1)", nativeQuery = true)
    AttendLog findByMaxAttendIdOfStudent(Long studentId);

    // event_name 을 변경하는 쿼리
    @Modifying
    @Query(value = "UPDATE attend_log SET event_name=?1 WHERE student_id=?2 ORDER BY event_attend_time DESC LIMIT 1",nativeQuery = true)
    int updateEventName(String eventName, Long studentId);

    // 최근 두개의 로그를 뽑기
    @Query(value = "SELECT * FROM attend_log WHERE student_id=?1 ORDER BY attend_log_id DESC LIMIT 2", nativeQuery = true)
    List<AttendLog> findLastTwoLog(Long studentId);
}
