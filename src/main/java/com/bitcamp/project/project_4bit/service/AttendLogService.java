package com.bitcamp.project.project_4bit.service;

import com.bitcamp.project.project_4bit.entity.AttendLog;
import com.bitcamp.project.project_4bit.repository.AttendLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    // 마지막에서 두번째 EventName 을 찾음
    @Transactional
    public AttendLog selectSecondAttendLog(Long studentId) {
        return attendLogRepository.findSecondEventNameByStudentId(studentId);
    }

    // 저장
    @Transactional
    public AttendLog createAttendLog(AttendLog attendLog){
        return attendLogRepository.save(attendLog);
    }


    // 수정
    @Transactional
    public int updateEvent(Long studentId, String eventName){
        return attendLogRepository.updateEventName(studentId, eventName);
    }
}
