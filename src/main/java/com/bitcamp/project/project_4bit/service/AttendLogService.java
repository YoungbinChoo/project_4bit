package com.bitcamp.project.project_4bit.service;

import com.bitcamp.project.project_4bit.entity.AttendLog;
import com.bitcamp.project.project_4bit.entity.Student;
import com.bitcamp.project.project_4bit.repository.AttendLogRepository;
import com.bitcamp.project.project_4bit.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class AttendLogService {

    @Autowired
    private AttendLogRepository attendLogRepository;

    @Autowired
    private StudentRepository studentRepository;

    //studentId 를 이용해서 AttendLog 를 찾는다
    @Transactional
    public AttendLog selectAttendLog(Long studentId){
        return attendLogRepository.findEventNameByStudentId(studentId);
    }

    // 저장
    @Transactional
    public AttendLog createAttendLog(AttendLog attendLog){
        return attendLogRepository.save(attendLog);
    }

    // 가장 최근의 학생 정보를 가져온다.
    @Transactional
    public AttendLog findLastAttendLog(Long studentId){
        return attendLogRepository.findByMaxAttendIdOfStudent(studentId);
    }

    // 출석 로그를 볼 수 있는 서비스
    @Transactional
    public Page<AttendLog> listOfAttendLogByStudentId(Long studentId, Pageable pageable){
        return attendLogRepository.findAllByStudent_StudentId(studentId, pageable);
    }

    // 유저아이디로 출석로그를 검색
    @Transactional
    public Page<AttendLog> listOfAttendLogByUserId(Long userId, Pageable pageable){
        return attendLogRepository.findAllByStudent_User_userId(userId, pageable);
    }

    // 모든 count를 0으로 바꾸는 서비스
    @Transactional
    public int updateAttendEventName(String eventName, Long studentId){
        return attendLogRepository.updateEventName(eventName, studentId);
    }

    // 최근 두개의 데이터를 뽑기
    @Transactional
    public List<AttendLog> findTwoLog(Long studentId){
        return attendLogRepository.findLastTwoLog(studentId);
    }
}
