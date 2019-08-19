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

    // 역할 : 학생이 입력한 답과 문제 답을 비교하여 획득한 점수를 반환한다
    public int compareStudentAnswer(Long studentTestId, Long testQuizId){

        /* ------------------------------------- [학생이 입력한 답 얻기] ------------------------------------- */
        // 1. studentId와 testQuizId를 이용해 student_answer 테이블에서 학생이 입력한 답을 가져온다
        String studentAnswer = studentAnswerService.readStudentAnswer(studentTestId, testQuizId);
        System.out.println("학생_답 : " +studentAnswer);

        // 만약 학생이 답을 입력하지 않아 반환값이 null이면 어떻게 처리해야할지 몰라서 일단 null로 했습니다
        if(studentAnswer == null)
            studentAnswer = "null";

        /* ------------------------------------- [문제 답 얻기] ------------------------------------- */
        // 2. testQuizId로 test_quiz 테이블에서 quiz_id를 얻어온다
        Long quizId = testQuizService.readQuizId(testQuizId);
        System.out.println("문제_번호 : " +quizId);

        // 3. quizId를 이용해 quiz 테이블에서 해당 문제 답(quizAnswer)을 얻는다
        String quizAnswer = quizService.readQuizAnswer(quizId);
        System.out.println("문제_답 : " +quizAnswer);

        // 4. 학생이 입력한 답과 문제 답을 비교하여
        if(studentAnswer.equals(quizAnswer)){
            System.out.println("답이 일치합니다");
            // 5. 맞으면 해당 문제의 점수를 반환하고
            return quizService.readQuizEachSCore(quizId);
        } else {
            System.out.println("답이 일치하지 않습니다");
            // 5. 그렇지 않으면 0을 반환한다
            return 0;
        }
    }

}
