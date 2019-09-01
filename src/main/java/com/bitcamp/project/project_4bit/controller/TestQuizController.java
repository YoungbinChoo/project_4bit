package com.bitcamp.project.project_4bit.controller;

import com.bitcamp.project.project_4bit.entity.Quiz;
import com.bitcamp.project.project_4bit.entity.TestGroup;
import com.bitcamp.project.project_4bit.entity.TestQuiz;
import com.bitcamp.project.project_4bit.service.QuizService;
import com.bitcamp.project.project_4bit.service.TestGroupService;
import com.bitcamp.project.project_4bit.service.TestQuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
/*
 * 작성일 : 2019.08.13
 * 수정일 : 2019.08.21
 * 순서
 * RequestMethod.타입, 메서드 명 : 설명
 * 1. POST,  createTestQuiz() : 시험문제 생성
 * 2. GET,  listOfOneTestQuiz() : 시험문제 1개 불러오기
 * 3. GET,   listOfTestQuiz() : 시험문제 전체보기
 * 4. PATCH,   updateTestQuiz() : 시험문제 수정
 * 5. DELETE,  deleteTestQuiz() : 시험문제 삭제
 *
 * 수정할때 프론트에서 이미 저장되어있던 정보가 그대로 보인 상태에서 수정변경 되어야 함.
 * 선생님 별로 삭제할 수 있는 권한?
 * */

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
//    endpoint : http://localhost:8080/class/testquiz/write?testId={testId}&quizId={quizId}
    @PreAuthorize("hasAnyAuthority('TEST_WRITE')")
    @RequestMapping(
            path = "/write",
            method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public TestQuiz createTestQuiz(
            @RequestBody TestQuiz testQuiz,
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

//    testQuiz 1개 상세 보기
//    endpoint : http://localhost:8080/class/testquiz/detail?testquizid={testQuizId}
    @PreAuthorize("hasAnyAuthority('TEST_READ')")
    @RequestMapping(
            path = "/view",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public TestQuiz listOfOneTestQuiz(
            @RequestParam(name = "testquizid", required = false) Long testQuizId){

        System.out.println("시험문제 id : " + testQuizId);

        return testQuizService.findByTestQuizId(testQuizId);
    }

//    testQuiz 전체
//    endpoint : http://localhost:8080/class/testquiz/list
    @PreAuthorize("hasAnyAuthority('TEST_READ')")
    @RequestMapping(
            path = "/list",
            method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Page<TestQuiz> listOfTestQuiz(Pageable pageable){
        return testQuizService.findAllOfTestQuiz(pageable);
    }

    /*  testQuiz 수정
     * 참고사항 : 퀴즈문제 수정은 선생님 고유 권한입니다. write_tquiz를 지정했습니다.
     *            반환형이 int인 이유는 update 쿼리문 사용하기 때문입니다.
     *            퀴즈의 내용, 정답, 배점, 과목, 챕터, 난이도를 수정할 수 있게 했습니다.
     *            quizId를 받아와 수정 할 수 있게 했습니다.
     * endpoint : http://localhost:8080/class/testquiz/{testquizId}
     * */
    @PreAuthorize("hasAnyAuthority('TEST_WRITE','TEST_READ')")
    @RequestMapping(
            path = "/{testquizId}",
            method = RequestMethod.PATCH,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE })
    public int updateTestQuiz(
            @PathVariable(name = "testquizId", required = false) Long testquizId,
            @RequestBody TestQuiz testQuiz){
        /*
         * 1. test_quiz_id : 고유번호 자동생성
         * 2. test_quiz_no : 시험 문제의 문제번호
         * 3. test_id : 시험의 고유번호 - test에서 받아와야함
         * 4. quiz_id : 문제의 고유번호 - quiz에서 받아와야함
         * */
        int testQuizNo = testQuiz.getTestQuizNo();
        Long testId = testQuiz.getTestGroup().getTestId();
        Long quizId = testQuiz.getQuiz().getQuizId();

        System.out.println("시험 : " + testId);
        System.out.println("문제 : " + quizId);

       return testQuizService.updateTestQuiz(testQuizNo, testId, quizId, testquizId);
    }

    // 역할 : 시험 삭제
    // 엔드포인트 : http://localhost:8080/class/test/testId={testId}/delete
    @PreAuthorize("hasAnyAuthority('TEST_WRITE')")
    @RequestMapping(
            path = "/delete/testquizId={testquizId}",
            method = RequestMethod.DELETE,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE})
    public TestQuiz deleteTestQuiz(@PathVariable("testquizId") Long testquizId) {
        System.out.println("시험문제번호 : " + testquizId);

        testQuizService.deleteTestQuiz(testquizId);

        TestQuiz testQuiz = new TestQuiz();
        testQuiz.setTestQuizId(testquizId);

        return testQuiz;
    }
}
