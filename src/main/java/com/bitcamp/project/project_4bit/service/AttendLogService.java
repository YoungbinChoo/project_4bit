package com.bitcamp.project.project_4bit.service;

import com.bitcamp.project.project_4bit.entity.AttendLog;
import com.bitcamp.project.project_4bit.repository.AttendLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class AttendLogService {

    @Autowired
    private AttendLogRepository attendLogRepository;


     // 마지막 EventName 을 찾는 것. (studentId 를 이용해서 AttendLog 를 찾는다)
    @Transactional
    public AttendLog selectAttendLog(Long studentId){
        return attendLogRepository.findEventNameByStudentId(studentId);
    }


    // 저장
    @Transactional
    public AttendLog createAttendLog(AttendLog attendLog){
        return attendLogRepository.save(attendLog);
    }

    @Transactional
    public AttendLog findAttendLog(Long studentId){
        return attendLogRepository.findByMaxAttendIdOfStudent(studentId);
    }

    // 학생 ID 로 학생 출석현황 조회
    @Transactional
    public AttendLog findStudentAttendLog(Long studentId){
        return attendLogRepository.findAllByStudent_StudentId(studentId);
    }

    @Transactional
    public Page<AttendLog> listOfAttendLogByStudentId(Long studentId, Pageable pageable){
        return attendLogRepository.findAllByStudent_StudentId(studentId, pageable);
    }
}
