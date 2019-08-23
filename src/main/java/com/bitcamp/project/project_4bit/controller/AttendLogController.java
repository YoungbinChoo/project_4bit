package com.bitcamp.project.project_4bit.controller;

import com.bitcamp.project.project_4bit.entity.AttendLog;
import com.bitcamp.project.project_4bit.entity.Student;
import com.bitcamp.project.project_4bit.service.AttendLogService;
import com.bitcamp.project.project_4bit.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/attend")
public class AttendLogController {

    @Autowired
    private AttendLogService attendLogService;

    @Autowired
    private StudentService studentService;

    @PreAuthorize("hasAnyRole('ROLE_GUEST')")
    @RequestMapping(
            method = RequestMethod.POST,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE})
    public AttendLog attendCheck(@RequestParam String attendId){

        // 1. 학생 정보를 찾아오기 위해서 attendId 로 학생정보를 select 해서 student에 저장
        Student student = studentService.selectStudentByAttendId(attendId);

        // 2. newAttend 의 student 엔티티에 위의 학생정보를 세팅함
        AttendLog newAttend = new AttendLog();
        newAttend.setStudent(student);

        // 3. attendId 의 소유자 학생의 studentId 를 이용해서 가장 마지막 AttendLog 의 eventName 을 찾아 OUT -> IN/ IN -> OUT 으로 바꿔서 세팅
        if(attendLogService.selectAttendLog(newAttend.getStudent().getStudentId()).getEventName().equals("INIT")
        || attendLogService.selectAttendLog(newAttend.getStudent().getStudentId()).getEventName().equals("OUT")){
            newAttend.setEventName("IN");
        }else{
            newAttend.setEventName("OUT");
        }

        return attendLogService.createAttendLog(newAttend);
    }
}
