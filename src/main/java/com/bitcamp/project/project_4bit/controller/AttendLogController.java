package com.bitcamp.project.project_4bit.controller;

import com.bitcamp.project.project_4bit.entity.Article;
import com.bitcamp.project.project_4bit.entity.AttendLog;
import com.bitcamp.project.project_4bit.entity.Student;
import com.bitcamp.project.project_4bit.entity.User;
import com.bitcamp.project.project_4bit.exception.AuthException;
import com.bitcamp.project.project_4bit.model.ResultItems;
import com.bitcamp.project.project_4bit.service.AttendLogService;
import com.bitcamp.project.project_4bit.service.LocalUserDetailsService;
import com.bitcamp.project.project_4bit.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.security.Principal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;

// 설명
// DB에서 꺼내오는 시간의 타입은 Timestamp.
// 자바에서 사용하는 시간 타입은 Date.
// .getTime() 으로 구하는 시간은 long 타입.
// 따라서 getTime() 으로 무작정 변환해서 비교하면 최초 기준이 달라 다른 시간이 구해진다.
// 그래서 같은 기준으로 만들어주는 작업이 필요
// String 형의 기준시간을 정하고 DB에서 꺼낸 시간을 String 형으로 바꾼다.
// 두 String 형의 시간들을 다시 Date 타입으로 바꾼다. (이때 yyyy-MM-dd 는 제외하고 HH:mm:ss 형태로 폼을 정해준다.)
// 그러면 년도.월.일 은 제외되고 시간을 가지고 같은 기준의 시간(Thu Han 01 HH:mm:ss KST 1970) 이 되서 비교가 가능하다.



@RestController
@RequestMapping("/attend")
public class AttendLogController {

    @Autowired
    private AttendLogService attendLogService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private LocalUserDetailsService userDetailsService;

    // 추후 수정.. POST 방식에서 GET 방식을 이용하는 거 수정하기.
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

        // 3. 현재 시간 세팅
        Date nowTime = new Timestamp(System.currentTimeMillis());
        newAttend.setEventAttendTime(nowTime);

        try{
            // 1. HH:mm:ss 의 형태로 세팅하기 위함
            // HH : 시간 mm : 분 ss : 초
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

            // 2. 지각의 기준이 되는 시간을 세팅(String 형)
            String str_standardLateTime = "09:30:59";

            // 3. 학원이 끝나는 시간을 세팅(String)
            String str_ClassEndTime = "18:10:59";

            // 4. Attend_Log 가 저장된 시간을 구해옴(Timestamp 형/ SQL에서 뽑아오는 것은 Date 형으로 선언해도 .getClass() 로 타입을 확인하면 timestamp 로 나온다.)
            Date db_AttendTime = attendLogService.selectAttendLog(newAttend.getStudent().getStudentId()).getEventAttendTime();


            // 5. 해당 학생의 가장 최근 출석기록의 시간을 구해온다. ( 조퇴의 기준을 잡기 위함 )
            Date db_AttendLastTime = attendLogService.findAttendLog(student.getStudentId()).getEventAttendTime();

            // 6.  같은 기준으로 만들기위해 일단 Date 형으로 형변환
            Date standardLateTime = new SimpleDateFormat("HH:mm:ss").parse(str_standardLateTime);
            Date classEndTime = new SimpleDateFormat("HH:mm:ss").parse(str_ClassEndTime);

            // 7.  formatter.format() 을 이용하여 우선 String 형으로 만들고 다시 Date 형으로 변환
            Date newAttendTime = new SimpleDateFormat("HH:mm:ss").parse(formatter.format(db_AttendTime));
            Date oldAttendTime = new SimpleDateFormat("HH:mm:ss").parse(formatter.format(db_AttendLastTime));

            // 8. (지각기준)시간
            // lateStandardTime < 0 이면 지각
            long lateStandardTime = standardLateTime.getTime() - newAttendTime.getTime();

            // 9. (결석기준)시간  absentStandardTime/60*1000 < 240 이면 결석/ > 240 이면 조퇴
            long absentStandardTime = newAttendTime.getTime() - oldAttendTime.getTime();

            // 10. (조퇴) 시간  earlyLeaveStandardTime > 0 조퇴 / < 0 퇴실
            long earlyLeaveStandardTime = classEndTime.getTime() - newAttendTime.getTime();

            // 10. attendId 의 소유자 학생의 studentId 를 이용해서 가장 마지막 AttendLog 의 eventName 을 찾아
            //    새롭게 eventName 을 세팅
            String lastAttendEventName = attendLogService.selectAttendLog(newAttend.getStudent().getStudentId()).getEventName();

            // 11. 조건에 따라 입실, 지각, 퇴실, 조퇴, 결석을 결정해줌
            System.out.println(lastAttendEventName + "-----------------------------------------------");
            switch(lastAttendEventName){
                case "INIT" :
                    // 지각인경우
                    if(lateStandardTime < 0)
                    {
                        // 결석
                        if(absentStandardTime/60000 < 240){
                            newAttend.setEventName("결석");
                        }else{
                        // 지각
                            newAttend.setEventName("지각");
                        }
                    }
                    // 지각이 아닌경우
                    else
                    {
                        // 결석
                        if(absentStandardTime/60000 < 240){
                            newAttend.setEventName("결석");
                        }else{
                            newAttend.setEventName("입실");
                        }
                    }
                    break;
                case "입실":
                case "지각":
                    if(absentStandardTime/60000 < 240){
                        newAttend.setEventName("결석");
                    }
                    else if(earlyLeaveStandardTime > 0 && absentStandardTime/60000 > 240){
                        newAttend.setEventName("조퇴");
                    }
                    else if(earlyLeaveStandardTime < 0){
                        newAttend.setEventName("퇴실");
                    }
                    break;
                case "퇴실":
                case "조퇴":
                case "결석":
                    if(lateStandardTime < 0){
                        newAttend.setEventName("지각");
                    }
                    else{
                        newAttend.setEventName("입실");
                    }
                    break;
                default:
                    break;
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return attendLogService.createAttendLog(newAttend);
    }


    // 학생 한명의 출석현황을 조회
    @PreAuthorize("hasAnyAuthority('CBOARD_READ','CNOTICE_READ','LIBRARY_READ')")
    @RequestMapping(
            path = "{studentId}",
            method = RequestMethod.GET,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE})
    public ResultItems<AttendLog> attendLog(Principal principal,
                                              @PathVariable("studentId") Long studentId,
                                              @RequestParam(name = "page", defaultValue = "1", required = false) int page,
                                              @RequestParam(name = "size", defaultValue = "10", required = false) int size){

        User user = (User) userDetailsService.loadUserByUsername(principal.getName());

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<AttendLog> attendLogList = attendLogService.listOfAttendLogByStudentId(studentId, pageable);


        if(user.getRole().getRoleCode().equals("role_admin")||user.getRole().getRoleCode().equals("role_teacher")){
            System.out.println("Role >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+user.getRole().getRoleCode());
            return new ResultItems<AttendLog>(attendLogList.stream().collect(Collectors.toList()), page, size, attendLogList.getTotalElements());
        }else if(user.getRole().getRoleCode().equals("role_student")){
            if(user.getUserId().equals((studentService.loadStudentByStudentId(studentId)).getUser().getUserId())){

                return new ResultItems<AttendLog>(attendLogList.stream().collect(Collectors.toList()), page, size, attendLogList.getTotalElements());
            }
        }
        throw new AuthException("해당 권한이 없습니다.");
    }

}
