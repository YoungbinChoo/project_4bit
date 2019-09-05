package com.bitcamp.project.project_4bit.controller;

import com.bitcamp.project.project_4bit.entity.AttendLog;
import com.bitcamp.project.project_4bit.entity.Student;
import com.bitcamp.project.project_4bit.entity.User;
import com.bitcamp.project.project_4bit.model.ResultItems;
import com.bitcamp.project.project_4bit.service.AttendLogService;
import com.bitcamp.project.project_4bit.service.LocalUserDetailsService;
import com.bitcamp.project.project_4bit.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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

    // 학생 한명의 출석현황을 조회
    @PreAuthorize("hasAnyAuthority('CBOARD_READ','CNOTICE_READ','LIBRARY_READ')")
    @RequestMapping(
            path = "{userId}",
            method = RequestMethod.GET,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE})
    public ResultItems<AttendLog> attendLog(Principal principal,
                                            @PathVariable("userId") Long userId,
                                            @RequestParam(name = "page", defaultValue = "1", required = false) int page,
                                            @RequestParam(name = "size", defaultValue = "10", required = false) int size){

        User user = (User) userDetailsService.loadUserByUsername(principal.getName());

//        Pageable pageable = PageRequest.of(page - 1, size);
        Pageable pageable = PageRequest.of((page < 1? 0 : page-1),(size<0?10:size), Sort.by("attendLogId").descending());
        Page<AttendLog> attendLogList = attendLogService.listOfAttendLogByUserId(userId, pageable);

        return new ResultItems<AttendLog>(attendLogList.stream().filter(t->!t.getEventName().equals("INIT")).collect(Collectors.toList()), page, size, attendLogList.getTotalElements());
    }



    // 1. 매일 01:00 에 모든 학생의 출석상태를 INIT 으로 등록
//    @Scheduled(cron = "0 0 1 ? * MON-FRI")
    @Scheduled(cron = "0 30 18 ? * *")
    public AttendLog InitAttendLog(){
        List<Student> students = studentService.itemsOfStudentsByClassId();

        for(int i = 0; i < students.size(); i++){
            AttendLog attendLog = new AttendLog();

            // 현재 시간을 세팅
            Date nowTime = new Timestamp(System.currentTimeMillis());
            attendLog.setEventAttendTime(nowTime);

            attendLog.setStudent(students.get(i));
            attendLogService.createAttendLog(attendLog);
        }
        return null;
    }

    //    // 2. 학생들이 출석 (조건에 맞게 입실, 지각, 퇴실, 조퇴 결정)
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
        long nowLongTime = nowTime.getTime();

        // 4. 가장 최근의 일일출석로그카운트를 찾아온 후 +1 한 다음 세팅
        int lastCount = attendLogService.findLastAttendLog(newAttend.getStudent().getStudentId()).getDailyAttendCount();
        newAttend.setDailyAttendCount(lastCount+1);


        try{
            // 1. HH:mm:ss 의 형태로 세팅하기 위함
            // HH : 시간 mm : 분 ss : 초
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

            // 2. 지각의 기준이 되는 시간을 세팅(String 형)
//            String str_standardLateTime = "09:30:00";
            String str_standardLateTime = "18:32:00";

            // 3. 학원이 끝나는 시간을 세팅(String)
//            String str_ClassEndTime = "18:10:00";
            String str_ClassEndTime = "18:34:00";


            // 4. 해당 학생의 가장 최근 출석기록의 시간을 구해온다. ( 조퇴의 기준을 잡기 위함 )
            Date db_AttendLastTime = attendLogService.findLastAttendLog(student.getStudentId()).getEventAttendTime();
            System.out.println("nowTime >>>>>>>>>>>>>>>>>>>>>>>>" + nowTime);
            System.out.println("db_AttendLastTime >>>>>>>>>>>>>>>>>>>>>>>>" + db_AttendLastTime);

            // 5.  같은 기준으로 만들기위해 일단 Date 형으로 형변환
            //     Thu Jan 01 09:00:00 KST 1970    부터 몇 ms가 지났나.
            //     09:30 -> 1800초가 지난것
            //     18:10 -> 33000 초가 지난것
            Date standardLateTime = new SimpleDateFormat("HH:mm:ss").parse(str_standardLateTime);
            Date classEndTime = new SimpleDateFormat("HH:mm:ss").parse(str_ClassEndTime);
            System.out.println("standardLateTime >>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + standardLateTime);
            System.out.println("classEndTime >>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + classEndTime);


            // 7.  formatter.format() 을 이용하여 우선 String 형으로 만들고 다시 Date 형으로 변환
            // 현재 로그가 남는 시간
            Date newAttendTime = new SimpleDateFormat("HH:mm:ss").parse(formatter.format(nowTime));
            // 가장 마지막의 로그의 시간
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


            // 12. 잘못 찍어 바로 퇴실이 된 사람의 경우를 위해 다시 찍었을 경우 가장 마지막 로그를 지우고 현재 로그를 다시 저장
            if(newAttend.getDailyAttendCount() == 3 && lastAttendEventName.equals("결석입니다") ||
                    newAttend.getDailyAttendCount() == 3 && lastAttendEventName.equals("결석") ||
                    newAttend.getDailyAttendCount() == 3 && lastAttendEventName.equals("조퇴")){

                // 해당 학생의 가장 마지막 로그를 삭제
                Long newAttendId = attendLogService.findLastAttendLog(newAttend.getStudent().getStudentId()).getAttendLogId();
                attendLogService.deleteAttendLog(newAttendId);

                // 일일출석횟수를 2로 바꾼 후 다시 세팅
                lastCount = 2;
                newAttend.setDailyAttendCount(lastCount);

                // 가장 마지막 출석상태를 가져옴
                String newLastAttendEventName = attendLogService.selectAttendLog(newAttend.getStudent().getStudentId()).getEventName();
                if(newLastAttendEventName.equals("입실")){
//                if(absentStandardTime/1000 > 14400 && earlyLeaveStandardTime > 0){
                    if(absentStandardTime/1000 > 60 && earlyLeaveStandardTime > 0){
                        newAttend.setEventName("조퇴");
                        return attendLogService.createAttendLog(newAttend);
                    }
//                else if(absentStandardTime/1000 > 14400 && earlyLeaveStandardTime < 0){
                    else if(absentStandardTime/1000 > 60 && earlyLeaveStandardTime < 0){
                        newAttend.setEventName("퇴실");
                        return attendLogService.createAttendLog(newAttend);
                    }
                    else {
                        newAttend.setEventName("결석입니다");
                        return attendLogService.createAttendLog(newAttend);
                    }
                }
                else if(newLastAttendEventName.equals("지각")){
//                    if(absentStandardTime/1000 >= 14400 && earlyLeaveStandardTime > 0){
                    if(absentStandardTime/1000 >= 60 && earlyLeaveStandardTime > 0){
                        newAttend.setEventName("조퇴");
                        return attendLogService.createAttendLog(newAttend);
                    }
//                else if(absentStandardTime/1000 >= 14400 && earlyLeaveStandardTime < 0) {
                    else if(absentStandardTime/1000 >= 60 && earlyLeaveStandardTime < 0) {
                        newAttend.setEventName("퇴실");
                        return attendLogService.createAttendLog(newAttend);
                    }else{
                        newAttend.setEventName("결석입니다");
                        return attendLogService.createAttendLog(newAttend);
                    }
                }else if(newLastAttendEventName.equals("조퇴")){
//                    if(absentStandardTime/1000 >= 14400 && earlyLeaveStandardTime > 0){
                    if(absentStandardTime/1000 >= 60 && earlyLeaveStandardTime > 0){
                        newAttend.setEventName("조퇴");
                        return attendLogService.createAttendLog(newAttend);
                    }
//                else if(absentStandardTime/1000 >= 14400 && earlyLeaveStandardTime < 0) {
                    else if(absentStandardTime/1000 >= 60 && earlyLeaveStandardTime < 0) {
                        newAttend.setEventName("퇴실");
                        return attendLogService.createAttendLog(newAttend);
                    }else{
                        newAttend.setEventName("결석입니다");
                        return attendLogService.createAttendLog(newAttend);
                    }
                }
                // 그 외의 조건에서는 null 로 리턴해서 나중에 일괄적으로 결석으로 처리
                else if(newLastAttendEventName.equals("퇴실")||newLastAttendEventName.equals("조퇴")){
                    return null;
                }

            }

            // 11. 조건에 따라 입실, 지각, 퇴실, 조퇴, 결석을 결정해줌
            if(lastAttendEventName.equals("INIT")){
                if(lateStandardTime < 0 ){
                    newAttend.setEventName("지각");
                    return attendLogService.createAttendLog(newAttend);
                }
                else if(lateStandardTime > 0){
                    newAttend.setEventName("입실");
                    return attendLogService.createAttendLog(newAttend);
                }
                else{
                    return null;
                }
            }
            else if(lastAttendEventName.equals("입실")){
//                if(absentStandardTime/1000 > 14400 && earlyLeaveStandardTime > 0){
                if(absentStandardTime/1000 > 60 && earlyLeaveStandardTime > 0){
                    newAttend.setEventName("조퇴");
                    return attendLogService.createAttendLog(newAttend);
                }
//                else if(absentStandardTime/1000 > 14400 && earlyLeaveStandardTime < 0){
                else if(absentStandardTime/1000 > 60 && earlyLeaveStandardTime < 0){
                    newAttend.setEventName("퇴실");
                    return attendLogService.createAttendLog(newAttend);
                }
                else {
                    newAttend.setEventName("결석입니다");
                    return attendLogService.createAttendLog(newAttend);
                }
            }
            else if(lastAttendEventName.equals("지각")){
//                if(absentStandardTime/1000 >= 14400 && earlyLeaveStandardTime > 0){
                if(absentStandardTime/1000 >= 60 && earlyLeaveStandardTime > 0){
                    newAttend.setEventName("조퇴");
                    return attendLogService.createAttendLog(newAttend);
                }
//                else if(absentStandardTime/1000 >= 14400 && earlyLeaveStandardTime < 0) {
                else if(absentStandardTime/1000 >= 60 && earlyLeaveStandardTime < 0) {
                    newAttend.setEventName("퇴실");
                    return attendLogService.createAttendLog(newAttend);
                }else{
                    newAttend.setEventName("결석입니다");
                    return attendLogService.createAttendLog(newAttend);
                }
            }
            // 그 외의 조건에서는 null 로 리턴해서 나중에 일괄적으로 결석으로 처리
            else if(lastAttendEventName.equals("퇴실")||lastAttendEventName.equals("조퇴")){
                return null;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    // 3. 매일 23시 50분에 결석 처리
//    @Scheduled(cron = "0 50 23 ? * MON-FRI")
    @Scheduled(cron = "0 35 18 ? * MON-FRI")
    public int updateEvent(){
        try{
            List<Student> students = studentService.itemsOfStudentsByClassId();
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

            for(int i = 0 ; i < students.size(); i++){
                AttendLog attendLog = new AttendLog();
                String str = "결석";

                // 학생 한명 세팅
                attendLog = attendLogService.selectAttendLog(students.get(i).getStudentId());

                if(attendLog.getDailyAttendCount() < 2)
                {
                    // 1. 아예 안왔을때
                    // 2. 퇴실을 안했을 때
                    Long studentId = attendLog.getStudent().getStudentId();
                    attendLogService.updateAttendEventName(str, studentId);
                }
                // 3. 수업시간이 240분이 안될때
                else if(attendLog.getDailyAttendCount() == 2)
                {
                    if(attendLog.getEventName().equals("결석입니다")){
                        attendLogService.updateAttendEventName(str,attendLog.getStudent().getStudentId());
                    }

                    // 그 학생에 대한 최근 2개의 로그를 가져옴
                    List<AttendLog> attendLogs = attendLogService.findTwoLog(attendLog.getStudent().getStudentId());

                    for (int j = 0; j < attendLogs.size(); j++) {

                        Date largeTime = attendLogs.get(0).getEventAttendTime();
                        Date smallTime = attendLogs.get(1).getEventAttendTime();

                        Date newLargeTime = new SimpleDateFormat("HH:mm:ss").parse(formatter.format(largeTime));
                        Date newSmallTime = new SimpleDateFormat("HH:mm:ss").parse(formatter.format(smallTime));

                        // 해당 학생의 최근 2개의 로그에대한 시간차이
                        long timeCal = newLargeTime.getTime() - newSmallTime.getTime();

                        // 그 시간차이가 240분(4시간) 보다 작다면 결석
//                        if (timeCal / 1000 < 14400) {
                        if (timeCal / 1000 < 60) {
                            attendLogService.updateAttendEventName(str,attendLog.getStudent().getStudentId());
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return 1;
    }
}