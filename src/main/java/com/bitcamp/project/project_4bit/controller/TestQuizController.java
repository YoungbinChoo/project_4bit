package com.bitcamp.project.project_4bit.controller;

import com.bitcamp.project.project_4bit.entity.Quiz;
import com.bitcamp.project.project_4bit.entity.TestGroup;
import com.bitcamp.project.project_4bit.entity.TestQuiz;
import com.bitcamp.project.project_4bit.service.QuizService;
import com.bitcamp.project.project_4bit.service.TestGroupService;
import com.bitcamp.project.project_4bit.service.TestQuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/class/testquiz")
public class TestQuizController {

    @Autowired
    private TestQuizService testQuizService;

    @Autowired
    private QuizService quizService;

    @Autowired
    private TestGroupService testGroupService;

    //    testQuiz 생성
    @PreAuthorize("hasAnyAuthority('TEST_WRITE')")
    @RequestMapping(
            path = "/write",
            method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public TestQuiz createQuiz(
            @RequestBody TestQuiz testQuiz,
//            @RequestParam(name = "number", required = false) int number,
            @RequestParam(name = "testId", required = false) Long testId,
            @RequestParam(name = "quizId", required = false) Long quizId
            ){
        /*
         * 1. test_quiz_id : 고유번호 자동생성
         * 2. test_quiz_no : 시험 문제의 문제번호
         * 3. test_id : 시험의 고유번호 - test에서 받아와야함
         * 4. quiz_id : 문제의 고유번호 - quiz에서 받아와야함
         * */

        System.out.println("시험 : " + testId);
        System.out.println("문제 : " + quizId);

        TestGroup testGroup = testGroupService.findOneByTestId(testId);
        Quiz quiz = quizService.findOneByQuiz(quizId);

        testQuiz.setTestGroup(testGroup);
        testQuiz.setQuiz(quiz);


        return testQuizService.createTestQuiz(testQuiz);
    }






}
