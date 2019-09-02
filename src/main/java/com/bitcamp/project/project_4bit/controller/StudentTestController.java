package com.bitcamp.project.project_4bit.controller;

import com.bitcamp.project.project_4bit.entity.StudentTest;
import com.bitcamp.project.project_4bit.entity.TestGroup;
import com.bitcamp.project.project_4bit.entity.User;
import com.bitcamp.project.project_4bit.service.*;
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
    private StudentTestService studentTestService;

    @Autowired
    private TestQuizService testQuizService;

    @Autowired
    private StudentAnswerController studentAnswerController;


    // 역할 : 응시하기 버튼을 누르면 생성되는 학생 시험 테이블
    // 엔드포인트 : http://localhost:8080/class/test/apply/testId={testId}
    // 시험 응시를 누르면 실행되어야 하는 메소드
    @PreAuthorize("hasAnyAuthority('STEST_WRITE')")
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


    // 역할 : 학생이 얻은 총점 구하기
    // 엔드포인트 : http://localhost:8080/class/answer/compare/testId={testId}/userId={userId}
   @RequestMapping(
            path = "/class/answer/compare/testId={testId}/userId={userId}",
            method = RequestMethod.PATCH,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public int updateStudentTest(@PathVariable("testId")Long testId, @PathVariable("userId")Long userId){
        int studentScore = 0;

        System.out.println("시험_번호 : " + testId);

        /* ------------------------------------- [학생 시험 번호 얻기] ------------------------------------- */
        // 1. testId와 userId로 통해 studentTestId를 얻는다
        Long studentTestId = studentTestService.readStudentTestId(testId, userId);
        System.out.println("학생_시험_번호 : " + studentTestId);

        /* ------------------------------------- [시험 문제 개수 얻기] ------------------------------------- */
        // 2. test_quiz 테이블에서 해당 testId로 생성된 quiz_id가 몇 개 있는지 세고(count) 그 개수를 반환하여 얻음
        int quizCount = testQuizService.readQuizCount(testId);
        System.out.println("문제_개수 : " + quizCount);

        /* ------------------------------------- [학생 점수 수정] ------------------------------------- */
        for(int i = 1; i <= quizCount; i++){
            // 모든 시험은 1번부터 시작하기에 i는 시험지 내 번호라고 할 수 있다
            // 3. testId와 i를 이용해 test_quiz 테이블에서 testQuizId를 구한다
            Long testQuizId = testQuizService.readTestQuizId(testId, i);
            System.out.println("시험_문제_번호 : " + testQuizId);

            // studentAnswerController내 compareStudentAnswer()를 이용
            /* 메소드 순서
               4-1. studentId와 testQuizId를 이용해 student_answer 테이블에서 학생이 입력한 답을 가져온다
               4-2. testQuizId로 test_quiz 테이블에서 quiz_id를 얻어온다
               4-3. quizId를 이용해 quiz 테이블에서 해당 문제 답(quizAnswer)을 얻는다
               4-4. 학생이 입력한 답과 문제 답을 비교하여
               4-5. 맞으면 해당 문제의 점수를 반환하고 그렇지 않으면 0을 반환한다*/
            int cnt = studentAnswerController.compareStudentAnswer(studentTestId, testQuizId);
            System.out.println(i+"번째_맞은_점수 : " + cnt);
            studentScore += cnt;

            // 5. 반환된 점수를 계속 더한다
            System.out.println(i+"번째_맞은_점수 : " + studentScore);
        }

        System.out.println("시험_총점 : " + studentScore);

        // 6. 앞서 구한 총점과 testId, userId를 이용해 student_test 테이블에서 개인시험점수를 수정한다
        //    >> 처음 시험을 볼 때는 0으로 생성이 되고 시험이 종료되었을 시 학생이 맞은 점수로 수정이 된다
        int successOrFail = studentTestService.updateStudentTest(studentScore, testId, userId);

        if(successOrFail == 0){
            System.out.println("수정에 실패했습니다");
        } else{
            System.out.println("수정에 성공했습니다");
        }

        return successOrFail;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 역할 : studentTestId가 있는지 구분
    //       기존 studentTestId가 있는 경우 studentAnswerController의 updateStudentAnswer()로 가고
    //       없는 경우 StudentTestCotroller의 createStudentTest()로 간다
    // 엔드포인트 :  http://localhost:8080/class/test/apply/testId={testId}/userId={userId}
    @RequestMapping(
            path = "/class/test/apply/testId={testId}/userId={userId}",
            method = RequestMethod.POST,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public void startStudentTest(
            @PathVariable("testId")Long testId,
            @PathVariable("userId")Long userId){

        Long studentTestId = studentTestService.readStudentTestId(testId, userId);

        if(studentTestId == null){
            System.out.println("학생 시험 번호가 없습니다");
//            createStudentTest();
        } else{
            System.out.println("이미 만들어진 학생 시험 번호가 있습니다 기존 시험 페이지로 이동합니다");
        }
    }

}
