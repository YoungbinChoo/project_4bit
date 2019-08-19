package com.bitcamp.project.project_4bit.controller;

import com.bitcamp.project.project_4bit.entity.StudentAnswer;
import com.bitcamp.project.project_4bit.entity.StudentTest;
import com.bitcamp.project.project_4bit.entity.TestQuiz;
import com.bitcamp.project.project_4bit.service.*;
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

    @Autowired
    private QuizService quizService;


    // 역할 : 학생이 답을 입력 >> 학생 답 테이블 생성
    // 엔드포인트 : http://localhost:8080/class/test/answer/write/studenttestId={studenttestid}/testquizid={testquizid}
    @PreAuthorize("hasAnyAuthority('STEST_WRITE')")
    @RequestMapping(
            method = RequestMethod.POST,
            path = "/class/test/answer/write/studenttestId={studenttestid}/testquizid={testquizid}",
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public StudentAnswer createStudentAnswer(
            Principal principal,
            @RequestBody StudentAnswer studentAnswer,
            @PathVariable("studenttestid") Long studenttestid,
            @PathVariable("testquizid") Long testquizid){

        System.out.println("학생_시험_번호 : " + studenttestid);

        StudentTest studentTest = studentTestService.findStudentTest(studenttestid);
        studentAnswer.setStudentTest(studentTest);

        System.out.println("시험_문제_번호 : " + testquizid);

        TestQuiz testQuiz = testQuizService.findByTestQuizId(testquizid);
        studentAnswer.setTestQuiz(testQuiz);

        return studentAnswerService.createStudentAnswer(studentAnswer);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 역할 : 학생이 입력한 답과 실제 문제 답을 비교
    // 엔드포인트 : http://localhost:8080/class/test/answer/compare/studentanswerid={studentanswerid}
    @RequestMapping(
            method = RequestMethod.GET,
            path = "/class/test/answer/compare/studentanswerid={studentanswerid}",
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public int compareStudentAnswer(
            @PathVariable("studentanswerid") Long studentanswerid){


        String studentAnswer = studentAnswerService.readStudentAnswer(studentanswerid);
        System.out.println("학생_답 : " +studentAnswer);

        Long testQuizId = studentAnswerService.readQuizTestId(studentanswerid);
        System.out.println("시험_문제_번호 : " +testQuizId);

        Long quizId = testQuizService.readQuizId(testQuizId);
        System.out.println("문제_번호 : " +quizId);

        String quizAnswer = quizService.readQuizAnswer(quizId);
        System.out.println("문제_답 : " +quizAnswer);


        if(studentAnswer.equals(quizAnswer)){
            System.out.println("답이 일치합니다");
            return quizService.readQuizEachSCore(quizId);
        } else {
            System.out.println("답이 일치하지 않습니다");
            return 0;
        }

    }
}
