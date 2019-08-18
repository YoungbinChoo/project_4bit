package com.bitcamp.project.project_4bit.controller;

import com.bitcamp.project.project_4bit.entity.Student;
import com.bitcamp.project.project_4bit.entity.User;
import com.bitcamp.project.project_4bit.service.LocalUserDetailsService;
import com.bitcamp.project.project_4bit.service.StudentService;
import com.bitcamp.project.project_4bit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/study/studentstatus")
public class CounselController {

    /*강사 학생현황 화면에서 상담 내역 가져오는 컨트롤러*/

    @Autowired
    private UserService userService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private LocalUserDetailsService userDetailsService;

    //1. 강사가 본인 담당 클래스의 학생 리스트를 가져온다. >> 학생 이름만 나오게 수정해야 하나?
    //endPoint : http://localhost:8080/study/studentstatus
    //@PreAuthorize() 현재 counsel 권한이 따로 없음
    @RequestMapping(
            method = RequestMethod.GET,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            })
    public Page<Student> listOf(Principal principal,
                                @RequestParam(name = "page", defaultValue = "1", required = false) int page,
                                @RequestParam(name = "size", defaultValue = "15", required = false) int size){

        // 1. 접속한 강사의 정보
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());

        //2. 접속한 강사로 classId를 찾는다.
        Long classId = userService.loadClassIdByUserId(user.getUserId());

        //3. classId로 학생 리스트를 뽑는다.
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Student> students = userService.listOfStudentByClassId(classId,pageable);

        return students;
    }


    /*todo : 학생 조회 및 상담내역 수정시 접속 강사 principal의 classId와 학생 classId 비교 체크*/


    //2. 강사가 학생 개인의 상담내역 가져온다.
    //endPoint : http://localhost:8080/study/studentstatus?studentId={studentId}
    //@PreAuthorize() 현재 counsel 권한이 따로 없음
    @RequestMapping(
            method = RequestMethod.POST,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            })
    public String readCounselText(Principal principal, @RequestParam("studentId")Long studentId){
        // 학생Id로 counsel 내용을 불러온다.
        String counselText = userService.loadCounselByStudentId(studentId);
        return counselText;
    }

    //3. 강사가 학생 개인의 상담내역을 작성, 수정한다.
    //endPoint : http://localhost:8080/study/studentstatus?studentId={studentId}
    //@PreAuthorize() 현재 counsel 권한이 따로 없음
    @RequestMapping(
            path = "/write",
            method = RequestMethod.PATCH,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            })
    public String updateCounselText(Principal principal, @RequestParam("studentId")Long studentId, @RequestBody Student student){

        String counselData = userService.loadCounselByStudentId(studentId);

        Student newStudent = student;
        String newCounsel = newStudent.getCounsel();

        if(newCounsel.equals(counselData)){
            return userService.updateCounselByTeacher(studentId,counselData);
        }else {
            return userService.updateCounselByTeacher(studentId,newCounsel);
        }
    }
}
