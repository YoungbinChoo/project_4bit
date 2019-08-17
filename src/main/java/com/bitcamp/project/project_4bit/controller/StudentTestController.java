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
    // 엔드포인트 : http://localhost:8080/class/test/apply/testId={testId}
    // 시험 응시를 누르면 실행되어야 하는 메소드
    @PreAuthorize("hasAnyAuthority('STEST_WRITE')")
    @RequestMapping(
            method = RequestMethod.GET,
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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 역할 : 학생들 점수 확인
    // 엔드포인트 : http://localhost:8080/study/endedtest/showscore/testno={testno}
    @PreAuthorize("hasAnyAuthority('STEST_READ')")
    @RequestMapping(
            method = RequestMethod.GET,
            path = "/study/endedtest/showscore/testId={testId}",
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public int showTestScore(
            Principal principal,
            @PathVariable("testId") Long testId) {

        System.out.println("조회_시험_번호 : " + testId);

        /* ------------------------------------- [userId 얻기] ------------------------------------- */
        // 1. principal을 이용해 user 전체 정보를 얻음
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());

        // 2. user 정보에서 userId를 얻음
        Long userId = user.getUserId();

        System.out.println("조회_유저_번호 : " + userId);

        int studentScore =  studentTestService.scoreOfStudent(testId, userId);

        if(studentScore == 0){
            System.out.println("해당 권한이 없거나 시험에 응시하지 않았습니다");

        }else {
            System.out.println("시험 점수는 " +studentScore+ "점입니다");
        }

        return studentScore;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 역할 : 점수 수정
    // 엔드포인트 : http://localhost:8080/class/test/apply/testId={testId}/edit
    // StudentAnswer이 생성될 때 실행되어야하는 메소드
    @PreAuthorize("hasAnyAuthority('STEST_WRITE')")
    @RequestMapping(
            path = "/class/test/apply/testId={testId}/edit",
            method = RequestMethod.PATCH,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public int updateStudentTest(
            Principal principal,
            @PathVariable("testId") Long testId,
            @RequestBody int studentScore){

        System.out.println("수정_시험_번호 : " + testId);

        /* ------------------------------------- [userId 얻기] ------------------------------------- */
        // 1. principal을 이용해 user 전체 정보를 얻음
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());

        // 2. user 정보에서 userId를 얻음
        Long userId = user.getUserId();

        System.out.println("수정_유저_번호 : " +userId);

        System.out.println("수정_시험_점수 : " + studentScore);
        
        /* ------------------------------------- [학생 점수 수정] ------------------------------------- */
        int successOrFail = studentTestService.updateStudentTest(studentScore, testId, userId);

        if(successOrFail == 0){
            System.out.println("수정에 실패했습니다");
        } else{
            System.out.println("수정에 성공했습니다");
        }

        return successOrFail;
    }


}
