package com.bitcamp.project.project_4bit.controller;

import com.bitcamp.project.project_4bit.entity.ClassGroup;
import com.bitcamp.project.project_4bit.entity.ConstraintDefine;
import com.bitcamp.project.project_4bit.entity.TestGroup;
import com.bitcamp.project.project_4bit.entity.User;
import com.bitcamp.project.project_4bit.model.ResultItems;
import com.bitcamp.project.project_4bit.service.*;
import com.bitcamp.project.project_4bit.util.UserIdToClassIdConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Collectors;

@RestController
public class TestGroupController {

    @Autowired
    private LocalUserDetailsService userDetailsService;

    @Autowired
    private TestGroupService testGroupService;

    @Autowired
    private ConstraintDefineService constraintDefineService;


    @Autowired
    private ClassGroupService classGroupService;


    @Autowired
    private UserIdToClassIdConverter userIdToClassIdConverter;

    @Autowired
    private StudentTestService studentTestService;


    @Autowired
    private TestQuizService testQuizService;



    // 역할 : 시험 작성
    // 주의사항 : teacherId를 구하기 위해 class_teacher_log 테이블 사용 >>  값이 들어있는지 확인해야해요
    // 엔드포인트 : http://localhost:8080/class/test/write
    @PreAuthorize("hasAnyAuthority('TEST_WRITE')")
    @RequestMapping(
            method = RequestMethod.POST,
            path = "/class/test/write",
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public TestGroup createTestGroup(
            Principal principal,
            @RequestBody TestGroup testGroup){
        /* ------------------------------------- [User 얻기] ------------------------------------- */
        // 1. principal을 이용해 user 전체 정보를 얻음
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        if (user.getRole().getRoleCode().equals("role_teacher")) {
            testGroup.setUser(user);


            /* ------------------------------------- [classId 얻기] ------------------------------------- */

            // 2. user 정보에서 userId를 얻음
            Long userId = user.getUserId();

            // 4. userIdToClassIdConverter에서 userId가 강사인 경우 classId를 찾는 방법을 통해 classId를 얻는다
            // TODO 권한 중요 >> 강사만 들어올 수 있게 해야함
            Long classId = userIdToClassIdConverter.userIdToClassId(userId);

            System.out.println("생성_반_Id : " + classId);

            // 5. classId로 classGroup 전체 정보를 얻는다 (DB : class_group / java : ClassGroup)
            ClassGroup classGroup = classGroupService.laodClassGroupByClassId(classId);
            testGroup.setClassGroup(classGroup);

            /* ------------------------------------- [ConstraintDefine 얻기] ------------------------------------- */
            // 시험만 사용할 것이기에 시험 제약 조건명은 test_constraint로 고정한다
            // 6. constraintName으로 constraintDefine 전체 정보를 얻는다 (DB : constraint_define / java : constraintDefine)
            ConstraintDefine constraintDefine = constraintDefineService.loadConstraintDefineByConstraintName("test_constraint");
            testGroup.setConstraintDefine(constraintDefine);


            return testGroupService.createTestGroup(testGroup);
        } else{
            return null;
        }
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////


    // 역할 : 반별 시험 진행 중 전체 출력
    // 주의사항 : Page<>로 받을 때 DB int형 값에 null이 있으면 에러 나요!! 채워주세요
    //          DB에 종료시간이 현재시간보다 이전인 것만 있으면 아무것도 나오지 않습니다
    // 엔드포인트 : http://localhost:8080/class/test
    @PreAuthorize("hasAnyAuthority('TEST_READ')")
    @RequestMapping(
            method = RequestMethod.GET,
            path = "/class/test",
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public ResultItems<TestGroup> listOfTestGroup(
            Principal principal,
            @RequestParam(name = "page", defaultValue = "1", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size){

        Pageable pageable = PageRequest.of(page - 1, size);
//        Pageable pageable = PageRequest.of((page < 1? 0 : page-1),(size<0?10:size), Sort.by("id").descending());


        /* ------------------------------------- [User 얻기] ------------------------------------- */
        // 1. principal으로 User정보 획득
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());

        /* ------------------------------------- [classId 얻기] ------------------------------------- */
        Long userId = user.getUserId();
        System.out.println("진행중_컨트롤_유저_Id : " + userId);

        // 여기서는 학생 강사 모두 읽기 권한을 가지고 있으므로 접근이 가능

        // 2. userId로 classId를 얻어옴
        Long classId = userIdToClassIdConverter.userIdToClassId(userId);

        System.out.println("진행중_컨트롤_반_Id : " + classId);


        /* ------------------------------------- [현재 시간 얻기] ------------------------------------- */

        Calendar calendar = Calendar.getInstance();
        // 3. Calendar를 통해 현재시간을 Date형으로 얻는다
        Date date = calendar.getTime();

        // 4. DB의 날짜 타입형(DATETIME)을 지키기위해 String형으로 변환한다
        //  EX) Date : Tue Aug 13 15:47:05 KST 2019
        //      String : 2019-08-13 15:47:05
        String today = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));

        System.out.println("진행중_Date형_지금_시간 : " + date);
        System.out.println("진행중_String형_지금_시간 : " + today);

        /* ------------------------------------- [시험 진행 중 리스트 얻기] ------------------------------------- */
        // ★ 여기서 DB : TestGroup 테이블에서 int형인 값(ex : sum, avg 등)에 null이 있으면 에러납니다
        Page<TestGroup> testGroupList = testGroupService.listOfTestGroup(classId, today, pageable);


        //5. 리턴 값이 있으면 출력
        if(testGroupList.getTotalElements() > 0) {
            return new ResultItems<TestGroup>(testGroupList.stream().collect(Collectors.toList()), page, size, testGroupList.getTotalElements());
        }else {
            //그렇지 않으면
            System.out.println("진행 중인 시험이 없습니다");
            return null;
        }
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////


    // 역할 : 반별 시험 진행 완료 전체 출력
    // 주의사항 : Page<>로 받을 때 DB int형 값에 null이 있으면 에러 나요!! 채워주세요
    // 엔드포인트 : http://localhost:8080/study/endedtest
    @PreAuthorize("hasAnyAuthority('TEST_READ')")
    @RequestMapping(
            method = RequestMethod.GET,
            path = "/study/endedtest",
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public ResultItems<TestGroup> listOfEndedTestGroup(
            Principal principal,
            @RequestParam(name = "page", defaultValue = "1", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
        Pageable pageable = PageRequest.of(page - 1, size);


        /* ------------------------------------- [User 얻기] ------------------------------------- */
        // 1. principal으로 User정보 획득
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());

        /* ------------------------------------- [userId 얻기] ------------------------------------- */
        Long userId = user.getUserId();
        System.out.println("완료_유저_Id : " + userId);

        /* ------------------------------------- [classId 얻기] ------------------------------------- */
        // 여기서는 학생 강사 모두 읽기 권한이 있으므로 접근이 가능

        // 2. userId로 classId를 얻어옴
        Long classId = userIdToClassIdConverter.userIdToClassId(userId);

        System.out.println("완료_반_Id : " + classId);

        /* ------------------------------------- [현재 시간 얻기] ------------------------------------- */
        Calendar calendar = Calendar.getInstance();
        // 3. Calendar를 통해 현재시간을 Date형으로 얻는다
        Date date = calendar.getTime();

        // 4. DB의 날짜 타입형(DATETIME)을 지키기위해 String형으로 변환한다
        String today = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));

        System.out.println("완료_지금_시간 : " + today);

        /* ------------------------------------- [시험 진행 완료 리스트 얻기] ------------------------------------- */
        // ★ 여기서 DB : TestGroup 테이블에서 int형인 값(ex : sum, avg 등)에 null이 있으면 에러납니다
        Page<TestGroup> testGroupList = testGroupService.listOfEndedTestGroup(classId, today, pageable);

        //5. 리턴 값이 있으면 출력
        if(testGroupList.getTotalElements() > 0) {
            return new ResultItems<TestGroup>(testGroupList.stream().collect(Collectors.toList()), page, size, testGroupList.getTotalElements());
        }else {
            //그렇지 않으면
            System.out.println("진행 완료인 시험이 없습니다");
            return null;
        }
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // 역할 : 시험 하나를 클릭했을 시 해당 시험 설명을 출력
    // 주의사항 : output은 TestGroup 전체지만 프론트에서 description만 출력하려고 계획 중
    // 엔드포인트 : http://localhost:8080/class/test/testId={testId}
    @PreAuthorize("hasAnyAuthority('TEST_READ')")
    @RequestMapping(
            method = RequestMethod.GET,
            path = "/class/test/testId={testId}",
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public TestGroup showDescriptionTest(
            Principal principal, @PathVariable("testId") Long testId){

        /* ------------------------------------- [User 얻기] ------------------------------------- */
        // 1. principal으로 User정보 획득
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());

        /* ------------------------------------- [userId 얻기] ------------------------------------- */
        Long userId = user.getUserId();
        System.out.println("설명_유저_Id : " + userId);

        /* ------------------------------------- [classId 얻기] ------------------------------------- */
        // 여기서는 학생 강사 모두 읽기 권한을 가지고 있으므로 접근이 가능

        // 2. userId로 classId를 얻어옴
        Long classId = userIdToClassIdConverter.userIdToClassId(userId);


        System.out.println("설명_반_Id : " + classId);

        System.out.println("상세_시험_번호 : "+ testId);
        // 반환형은 TestGroup인데 얻어오는 타입은 Optional<>형이므로 .get()을 해줘야 한다
        return testGroupService.itemOfTestGroup(classId, testId).get();
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // 역할 : 시험 응시하기 버튼을 클릭했을 시 응시 가능 기간인지를 판별하여 출력
    // 엔드포인트 : http://localhost:8080/class/test/testId={testId}/apply
    @PreAuthorize("hasAnyAuthority('STEST_WRITE')")
    @RequestMapping(
            method = RequestMethod.GET,
            path = "/class/test/testId={testId}/apply",
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public Long checkTimeApplyTest(
            Principal principal, @PathVariable("testId") Long testId){

        /* ------------------------------------- [classId 얻기] ------------------------------------- */
        // principal으로 User정보 획득
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());

        Long userId = user.getUserId();

        // 여기서는 학생만이 시험 응시 권한을 가지고 있으므로 학생만 접근해야함

        // 4. userIdToClassIdConverter에서 userId가 학생인 경우 classId를 찾는 방법을 통해 classId를 얻는다
        // TODO 권한 중요 >> 학생만 들어올 수 있게 해야함
        Long classId = userIdToClassIdConverter.userIdToClassId(userId);

        System.out.println("컨트롤_유저_Id : " + userId);
        System.out.println("컨트롤_반_Id : " + classId);

        /* ------------------------------------- [현재 시간 얻기] ------------------------------------- */
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String today = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));

        System.out.println("지금_시간 : " + today);


        /* ------------------------------------- [시험 응시 가능 시간 여부 확인] ------------------------------------- */
        Long applyTestId = testGroupService.applyItemOfTestGroup(classId, testId, today);


        if(applyTestId == null) {
            System.out.println("시험_번호 : " + applyTestId);
            System.out.println("시험_여부 : 시험 응시 기간이 아니거나 권한 없음(다른 반)");
            return 0L;
        }

        else {
            System.out.println("시험_번호 : " + applyTestId);
            System.out.println("시험_여부 : 시험 응시 가능한 기간입니다");
            return applyTestId;
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // 역할 : 시험 수정
    // 엔드포인트 : http://localhost:8080/class/test/testId={testId}/edit
    @PreAuthorize("hasAnyAuthority('TEST_WRITE')")
    @RequestMapping(
            path = "/class/test/testId={testId}/edit",
            method = RequestMethod.PATCH,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public int updateTestGroup(
            Principal principal,
            @PathVariable("testId") Long testId,
            @RequestBody TestGroup testGroup) {
        int trueOrFalse =0;

        Long testOwner = testGroupService.findTestGroupOwnerId(testId);
        System.out.println("작성자 : " + testOwner);

        /* ------------------------------------- [User 얻기] ------------------------------------- */
        // principal으로 User정보 획득
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());

        /* ------------------------------------- [teacherId 얻기] ------------------------------------- */
        // userId로 teacher에서 teacherId를 획득
        Long userId = user.getUserId();
        System.out.println("유저_번호 : " + userId);
        // 4. userIdToClassIdConverter에서 userId가 강사인 경우 classId를 찾는 방법을 통해 classId를 얻는다
        Long classId = userIdToClassIdConverter.userIdToClassId(userId);

        System.out.println("컨트롤_반_Id : " + classId);

        /* ------------------------------------- [수정 실행] ------------------------------------- */

        // 시간을 수정할 때 Date형으로 받으므로 String형으로 변환한다(DB에서의 DATETIME형을 맞추기 위해)
        System.out.println("변환_전_시작_시간" + testGroup.getTestStartTime());
        System.out.println("변환_전_종료_시간" + testGroup.getTestEndTime());

//        Date startTime = testGroup.getTestStartTime();
//        String testStartTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime));
//
//        Date endTime = testGroup.getTestEndTime();
//        String testEndTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTime));
//
//        System.out.println("변환_후_시작_시간" + testStartTime);
//        System.out.println("변환_후_종료_시간" + testEndTime);

        if (user.getRole().getRoleCode().equals("role_teacher")) {
            if (testOwner == userId) {
                trueOrFalse
                        = testGroupService.updateTestGroup(testGroup.getTestName(), testGroup.getTestStartTime(), testGroup.getTestEndTime(), testGroup.getTestDescription(), testId, classId);

                if(trueOrFalse == 0){
                    System.out.println("수정에 실패했습니다");
                } else {
                    System.out.println("수정에 성공했습니다");
                }
            }
        }

        return trueOrFalse;
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // 역할 : 시험 삭제
    // 엔드포인트 : http://localhost:8080/class/test/testId={testId}/delete
    @PreAuthorize("hasAnyAuthority('TEST_WRITE')")
    @RequestMapping(
            path = "/class/test/testId={testId}/delete",
            method = RequestMethod.DELETE,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public TestGroup deleteTestGroup(
            Principal principal,
            @PathVariable("testId") Long testId) {

        Long testOwner = testGroupService.findTestGroupOwnerId(testId);
        System.out.println("작성자 : " + testOwner);

//      /* ------------------------------------- [userId 얻기] ------------------------------------- */
        // principal으로 User정보 획득
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());

        Long userId = user.getUserId();
        System.out.println("유저_번호 : " + userId);

        if (user.getRole().getRoleCode().equals("role_teacher")) {
            if (testOwner == userId) {

                System.out.println("시험_번호 : " + testId);

                testGroupService.deleteTestGroup(testId);
                testQuizService.deleteTestQuizByTestId(testId);

                System.out.println("삭제_성공");
//                TestGroup testGroup = new TestGroup();
//                testGroup.setTestId(testId);
//                return testGroup;
            }
        }

        return null;
    }




    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 역할 : 해당 시험 점수 총점 및 평균 수정
    // 엔드포인트 : http://localhost:8080/class/test/testId={testId}/update
    @RequestMapping(
            path = "/class/test/testId={testId}/update",
            method = RequestMethod.GET,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public int updateScores(@PathVariable("testId") Long testId){

        int sum = studentTestService.readSumByTestId(testId);
        System.out.println("총점 : " + sum);

        double avg = sum/studentTestService.readStudentCountByTestId(testId);
        System.out.println("평균 : " + avg);

        int max = studentTestService.readMaxByTestId(testId);
        System.out.println("최고점 : " + max);

        int min = studentTestService.readMinByTestId(testId);
        System.out.println("최저점 : " + min);

        int successOrFail = testGroupService.updateStudentScore(sum, avg, max, min, testId);

        if(successOrFail == 0){
            System.out.println("수정에 실패했습니다");
        } else {
            System.out.println("수정에 성공했습니다");
        }

        return successOrFail;
    }

}