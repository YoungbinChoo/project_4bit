package com.bitcamp.project.project_4bit.controller;

import com.bitcamp.project.project_4bit.entity.ConstraintDefine;
import com.bitcamp.project.project_4bit.entity.Quiz;
import com.bitcamp.project.project_4bit.entity.User;
import com.bitcamp.project.project_4bit.model.ResultItems;
import com.bitcamp.project.project_4bit.service.ConstraintDefineService;
import com.bitcamp.project.project_4bit.service.LocalUserDetailsService;
import com.bitcamp.project.project_4bit.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.stream.Collectors;

/*
 * 작성일 : 2019.08.13
 * 순서
 * RequestMethod.타입, 메서드 명 : 설명
 * 1. POST,  createQuiz() : 퀴즈 생성
 * 2. GET,   listOfAllQuiz() : 전체퀴즈문제 불러오는 메서드
 * 3. GET,   listOfQuizSubjectRetrieve() : 과목별 퀴즈문제 불러오는 메서드
 * 4. GET,   listOfQuizChapterRetrieve() : 챕터별 퀴즈문제 불러오는 메서드
 * 5. GET,   listOfQuizLevelRetrieve() : 난이도별 퀴즈문제 불러오는 메서드
 * 6. PATCH, updateQuiz() : 퀴즈 수정
 *
 * 검색할때 프론트 단에서 과목별인지 챕터별인지 난이도별인지 구분해줄 수 있어야 함.
 * 수정할때 프론트에서 이미 저장되어있던 정보가 그대로 보인 상태에서 수정변경 되어야 함.
 * */

@RestController
@RequestMapping("/class/test/exbank")
public class QuizController {


    @Autowired
    private LocalUserDetailsService userDetailsService;

    @Autowired
    private ConstraintDefineService constraintDefineService;

    @Autowired
    private QuizService quizService;

    /* 퀴즈 생성
     * input : param으로 사용자가 입력한 quiz가 전체 담겨옵니다.
     * 참고사항 : 선생님 고유 권한이니까 TEST_WRITE 지정했습니다.
     * endpoint : http://localhost:8080/class/test/exbank/write
     * */
    @PreAuthorize("hasAnyAuthority('TEST_WRITE')")
    @RequestMapping(
            path = "/write",
            method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Quiz createQuiz(Principal principal, @RequestBody Quiz quiz){

//        principal에서 username을 얻어온 후, userDetailService로 User정보 덩어리 받아옴
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
//        제약이름이 "quiz_constraint" 인 것을 찾아서 준비
        ConstraintDefine constraintDefine = constraintDefineService.loadConstraintDefineByConstraintName("quiz_constraint");

//        받아온 user와 constraintDefine를 quiz에 담아서 준비
        quiz.setUser(user);
        quiz.setConstraintDefine(constraintDefine);

//        완전히 모든항목이 세팅된 quiz를 quizService에게 인자로 넘겨주기
        return quizService.createQuiz(quiz);
    }

    /*  퀴즈 전체 목록 불러오기
     * 참고사항 : 문제 생성및 보기는 선생님의 읽기 쓰기 권한이 필요합니다.
     *           전체 리스트를 보여주면 되기 때문에 따로 외부에서 받아오는 param은 없고
     *           pageable 을 해줘야해서 ResultItems을 했습니다.
     * endpoint : http://localhost:8080/class/test/exbank/list
     * */
    @PreAuthorize("hasAnyAuthority('TEST_READ')")
    @RequestMapping(
            path = "/list",
            method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResultItems<Quiz> listOfAllQuiz(
            @RequestParam(name = "page", defaultValue = "1", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size){

        Pageable pageable = PageRequest.of(page-1, size);
        Page<Quiz> quizList = quizService.findAllByQuiz(pageable);

        return new ResultItems<Quiz>(quizList.stream().collect(Collectors.toList()), page, size, quizList.getTotalElements());
    }

    // Todo: 이부분을 퀴즈 상세보기로 고쳐야(quiz id받아서 내용표시)
//     http://localhost:8080/class/test/exbank/oneList?quizId={quizId}
    @PreAuthorize("hasAnyAuthority('TEST_READ')")
    @RequestMapping(
            path = "/detail",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Quiz listOfOneQuiz(
            @RequestParam(name = "quizId", required = false) Long quizId){

            return quizService.findOneByQuiz(quizId);
    }

    /* 퀴즈 검색
     * 참고사항 : 문제 보기는 선생님의 읽기 권한이 필요
     * 외부에서 quizSubject, quizChapter, quizLevel를 파라미터를 @RequestBody로 받아옴
     * endpoint : http://localhost:8080/class/test/exbank/retrieve?subject={quizSubject}&chapter={quizChapter}&level={quizLevel}*/
    @PreAuthorize("hasAnyAuthority('TEST_READ')")
    @RequestMapping(
            path = "/retrieve",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResultItems<Quiz> listOfQuizRetrieve(
            @RequestParam(name = "subject", required = false) String quizSubject,
            @RequestParam(name = "chapter", required = false) String quizChapter,
            @RequestParam(name = "level", required = false) String quizLevel,
            @RequestParam(name = "page", defaultValue = "1", required = false) int page,
            @RequestParam(name = "size", defaultValue = "5", required = false) int size){

        Pageable pageable = PageRequest.of(page-1, size);
        System.out.println("과목 : " + quizSubject);
        System.out.println("챕터 : " + quizChapter);
        System.out.println("난이도 : " + quizLevel);
        Page<Quiz> quizList = quizService.findQuizByQuizSubjectAndQuizChapterAndQuizLevel(pageable, quizSubject,quizChapter,quizLevel);

//        return quizList;
        return new ResultItems<Quiz>(quizList.stream().collect(Collectors.toList()), page, size, quizList.getTotalElements());
    }


    /*  퀴즈 수정
     * 참고사항 : 퀴즈문제 수정은 선생님 고유 권한입니다. write_tquiz를 지정했습니다.
     *            반환형이 int인 이유는 update 쿼리문 사용하기 때문입니다.
     *            퀴즈의 내용, 정답, 배점, 과목, 챕터, 난이도를 수정할 수 있게 했습니다.
     *            quizId를 받아와 수정 할 수 있게 했습니다.
     * endpoint : http://localhost:8080/class/test/exbank/{quizId}
     * */
    @PreAuthorize("hasAnyAuthority('TEST_WRITE')")
        @RequestMapping(
                path = "/{quizId}",
                method = RequestMethod.PATCH,
                produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE })
        public int updateQuiz(
                Principal principal,
                @PathVariable(name = "quizId", required = true) Long quizId,
                @RequestBody Quiz quiz) {

        //////////// param으로 넘어오는 homework의 구성 //////////////////////////////
        // 1. quiz_id  : 자동 생성 (건드리지 않음)
        // 2. quiz_Contents  : quiz에서 받아옴
        // 3. quiz_Answer  : quiz에서 받아옴
        // 4. quiz_each_score  : 선생님이 입력 하는게 아니고 레벨이 상중하 에 따라 자동 지정 : 중 5 상 7 하 3
        // 5. quiz_subject  : quiz에서 받아옴
        // 6. quiz_chapter  : quiz에서 받아옴
        // 7. quiz_level  : quiz에서 받아옴
        // 8. quiz_answerType  : quiz에서 받아옴
        // 9. quiz_explain  : quiz에서 받아옴
        // 10. user_id  : (컨트롤러에서 principal기반으로 입력해줘야)
        // 11. constraint_name  : (컨트롤러에서 hardfix로 입력해주면 될듯)
        ///////////////////////////////////////////////////////////////////////////////

        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        ConstraintDefine constraintDefine = constraintDefineService.loadConstraintDefineByConstraintName("quiz_constraint");
        quiz.setUser(user);
        quiz.setConstraintDefine(constraintDefine);

        return quizService.updateQuiz(quiz.getQuizContents(), quiz.getQuizAnswer(), quiz.getQuizEachScore(), quiz.getQuizSubject(), quiz.getQuizChapter(), quiz.getQuizLevel(), quizId);
    }

}
