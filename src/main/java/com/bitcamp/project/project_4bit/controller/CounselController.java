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

/*작성자 : 황서영
* 컨트롤러에 권한 따로 주지 않았는데 담당강사의 클래스 아이디와 학생 클래스 아이디를 비교하기 때문에 따로 필요 없을 것 같음
* */

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

        if(user.getRole().getRoleCode().equals("role_teacher")|| user.getRole().getRoleCode().equals("role_admin")){
            //2. 접속한 강사로 classId를 찾는다.
            Long classId = userService.loadClassIdByUserId(user.getUserId());

            //3. classId로 학생 리스트를 뽑는다.
            Pageable pageable = PageRequest.of(page-1, size);
            Page<Student> students = userService.listOfStudentByClassId(classId,pageable);

            return students;
        }else {
            return null; // 강사나 어드민이 아니면 null 반환
        }


    }

    //2. 강사가 학생 개인의 상담내역 가져온다.
    //endPoint : http://localhost:8080/study/studentstatus?studentId={studentId}
    @RequestMapping(
            method = RequestMethod.POST,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            })
    public String readCounselText(Principal principal, @RequestParam("studentId")Long studentId){

        // 1. 접속한 강사의 정보
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());

        if(user.getRole().getRoleCode().equals("role_teacher")|| user.getRole().getRoleCode().equals("role_admin")){
            //2. 접속한 강사로 classId를 찾는다.
            Long classId = userService.loadClassIdByUserId(user.getUserId());

            //3. 학생 아이디로 ClassId를 찾는다.
            Long StudentClassId = userService.loadClassIdByStudentId(studentId);

            // 강사의 클래스 아이디랑 학생의 클래스 아이디를 비교한다.
            if(classId == StudentClassId || user.getRole().getRoleCode().equals("role_admin")){
                // 학생Id로 counsel 내용을 불러온다.
                String counselText = userService.loadCounselByStudentId(studentId);
                return counselText;
            }else {
                return "담당 클래스의 학생이 아니어서 상담내역을 읽어올 수 없습니다.";
            }
        }else {
            return "권한이 없습니다.";
        }

    }

    //3. 강사가 학생 개인의 상담내역을 작성, 수정한다.
    //endPoint : http://localhost:8080/study/studentstatus?studentId={studentId}
    @RequestMapping(
            path = "/write",
            method = RequestMethod.PATCH,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            })
    public String updateCounselText(Principal principal, @RequestParam("studentId")Long studentId, @RequestBody Student student){

        //기존에 저장된 상담내역
        String counselData = userService.loadCounselByStudentId(studentId);

        //새로운 상담내역
        Student newStudent = student;
        String newCounsel = newStudent.getCounsel();

        // 1. 접속한 강사의 정보
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());

        if(user.getRole().getRoleCode().equals("role_teacher")|| user.getRole().getRoleCode().equals("role_admin")){
            //2. 접속한 강사로 classId를 찾는다.
            Long classId = userService.loadClassIdByUserId(user.getUserId());

            //3. 학생 아이디로 ClassId를 찾는다.
            Long StudentClassId = userService.loadClassIdByStudentId(studentId);

            // 강사의 클래스 아이디랑 학생의 클래스 아이디를 비교한다.
            if(classId == StudentClassId || user.getRole().getRoleCode().equals("role_admin")){
                if(newCounsel.equals(counselData)){
                    return userService.updateCounselByTeacher(studentId,counselData);
                }else {
                    return userService.updateCounselByTeacher(studentId,newCounsel);
                }
            }else {
                return "담당 클래스의 학생이 아니어서 상담내역을 수정할 수 없습니다.";
            }
        }else {
            return "권한이 없습니다.";
        }
    }
}
