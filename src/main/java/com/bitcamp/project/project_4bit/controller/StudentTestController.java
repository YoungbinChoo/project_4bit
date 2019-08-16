package com.bitcamp.project.project_4bit.controller;

import com.bitcamp.project.project_4bit.entity.StudentTest;
import com.bitcamp.project.project_4bit.entity.TestGroup;
import com.bitcamp.project.project_4bit.entity.User;
import com.bitcamp.project.project_4bit.service.LocalUserDetailsService;
import com.bitcamp.project.project_4bit.service.StudentTestService;
import com.bitcamp.project.project_4bit.service.TestGroupService;
import com.bitcamp.project.project_4bit.util.UserIdToClassIdConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class StudentTestController {

    @Autowired
    private LocalUserDetailsService userDetailsService;

    @Autowired
    private TestGroupService testGroupService;

    @Autowired
    private UserIdToClassIdConverter userIdToClassIdConverter;

    @Autowired
    private StudentTestService studentTestService;


    // 역할 : 응시하기 버튼을 누르면 생성되는 학생 시험 테이블
    // 엔드포인트 : /class/test/apply/testId={testId}
    @PreAuthorize("hasAnyAuthority('WRITE_STEST')")
    @RequestMapping(
            method = RequestMethod.POST,
            path = "/class/test/apply/testId={testId}",
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public StudentTest createStudentTest(
            Principal principal,
            @RequestBody StudentTest studentTest,
            @PathVariable("testId") Long testId){

        /* ------------------------------------- [User 얻기] ------------------------------------- */
        // 1. principal으로 User정보 획득
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());

        studentTest.setUser(user);

        /* ------------------------------------- [TestGruop 얻기] ------------------------------------- */

        TestGroup testGroup = testGroupService.loadTestGroupBytestId(testId);
        studentTest.setTestGroup(testGroup);


        return studentTestService.createStudentTest(studentTest);
    }


    // 역할 : 학생들 점수 확인

}
