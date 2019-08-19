package com.bitcamp.project.project_4bit.controller;

import com.bitcamp.project.project_4bit.entity.StudentAnswer;
import com.bitcamp.project.project_4bit.entity.StudentTest;
import com.bitcamp.project.project_4bit.entity.TestQuiz;
import com.bitcamp.project.project_4bit.service.LocalUserDetailsService;
import com.bitcamp.project.project_4bit.service.StudentAnswerService;
import com.bitcamp.project.project_4bit.service.StudentTestService;
import com.bitcamp.project.project_4bit.service.TestQuizService;
import com.bitcamp.project.project_4bit.util.UserIdToClassIdConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class StudentAnswerController {

    @Autowired
    private LocalUserDetailsService userDetailsService;

    @Autowired
    private UserIdToClassIdConverter userIdToClassIdConverter;

    @Autowired
    private StudentTestService studentTestService;

    @Autowired
    private TestQuizService testQuizService;

    @Autowired
    private StudentAnswerService studentAnswerService;


    // 역할 : 학생이 답을 입력 >> 학생 답 테이블 생성
    // 엔드포인트 : http://localhost:8080/class/test/write
    @PreAuthorize("hasAnyAuthority('STEST_WRITE')")
    @RequestMapping(
            method = RequestMethod.POST,
            path = "/class/test/answer/write/studentTestId={studentTestId}/testQuizId={testQuizId}",
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public StudentAnswer createStudentAnswer(
            Principal principal,
            @RequestBody StudentAnswer studentAnswer,
            @PathVariable("studentTestId") Long studentTestId,
            @PathVariable("testQuizId") Long testQuizId){

        System.out.println("학생_시험_번호 : " + studentTestId);

        StudentTest studentTest = studentTestService.findStudentTest(studentTestId);
        studentAnswer.setStudentTest(studentTest);

        System.out.println("시험_문제_번호 : " + testQuizId);

        TestQuiz testQuiz = testQuizService.findByTestQuizId(testQuizId);
        studentAnswer.setTestQuiz(testQuiz);

        return studentAnswerService.createStudentAnswer(studentAnswer);
    }
}
