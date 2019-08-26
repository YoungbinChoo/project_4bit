package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.AttendLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface AttendLogRepository extends JpaRepository<AttendLog, Long> {


    // student_id 로 attend_log 를 찾음
    @Query(value ="SELECT * FROM attend_log WHERE student_id = ?1 ORDER BY event_attend_time DESC LIMIT 1", nativeQuery = true)
    AttendLog findEventNameByStudentId(Long studentId);

    // event_name 을 수정
    @Modifying
    @Query(value = "UPDATE attend_log SET event_name =?2 WHERE student_id =?1", nativeQuery = true)
    int updateEventName(Long studetnId, String eventName);

    // event_attend_time 이 두 번째로 큰 값을 찾음 ( 조퇴체크를 위함)
    @Query(value ="SELECT * FROM attend_log WHERE student_id = ?1 ORDER BY event_attend_time DESC LIMIT 1,1", nativeQuery = true)
    AttendLog findSecondEventNameByStudentId(Long studentId);


}
