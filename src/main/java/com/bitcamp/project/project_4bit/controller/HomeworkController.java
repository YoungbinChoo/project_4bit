/*
최상위 도메인: /class/assignment

create : /class/assignment/write                POST    새 과제물 생성(강사만 가능)
listOf : /class/assignment/list                 POST     현재 진행중인 과제 목록
retrieve : /class/assignment/view?hwno={hwno}   GET     과제 상세보기
update : /class/assignment/view?hwno={hwno}     PATCH   과제 수정
delete : /class/assignment/view?hwno={hwno}     DELETE  과제 삭제
*/

package com.bitcamp.project.project_4bit.controller;

import com.bitcamp.project.project_4bit.entity.*;
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
@RequestMapping("/class/assignment")
public class HomeworkController {

    @Autowired
    private LocalUserDetailsService userDetailsService;

    @Autowired
    private HomeworkService homeworkService;

    @Autowired
    private ConstraintDefineService constraintDefineService;

    @Autowired
    private ClassGroupService classGroupService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private ClassTeacherLogService classTeacherLogService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private UserIdToClassIdConverter userIdToClassIdConverter;


    // Todo: 권한체크 강화? (authority체크만으로 넘어갈거면 불필요) - 불필요한듯
    ///////////////////////////   Homework 출제(=글쓰기)   ///////////////////////////
    // http://localhost:8080/class/assignment/write
    @PreAuthorize("hasAnyAuthority('HW_WRITE')")    // 강사만 가능
    @RequestMapping(
            path = "/write",
            method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Homework create(Principal principal, @RequestBody Homework homework) {

        //////////// param으로 넘어오는 homework의 구성 //////////////////////////////
        // 1. 과제_고유번호 : homework에 없음(사용자 입력X, 자동생성돼야)
        // 2. 과제명 : homework에 담겨옴(사용자 입력)
        // 3. 제출기한 : homework에 담겨옴(사용자 입력)
        // 4. 과제출제일 : homework에 없음
        // 5. 과제수정일 : homework에 없음
        // 6. 과제과목 : homework에 담겨옴(사용자 입력)
        // 7. 과제내용 : homework에 담겨옴(사용자 입력)
        // 8. 과제제출파일첨부유무 : homework에 없음 (컨트롤러에서 입력해줘야)
        // 9. 유저_고유번호 : homework에 없음 (컨트롤러에서 principal기반으로 입력해줘야)
        // 10. 제약이름 : homework에 없음 (컨트롤러에서 hardfix로 입력해주면 될듯)
        // 11. 반_고유번호 : homework에 없음 (컨트롤러에서 principal기반으로 입력해줘야)
        ///////////////////////////////////////////////////////////////////////////////


        // 4. 과제출제일 & 5. 과제수정일 세팅
        Date date = new Date();     // 새로운 시스템 시간 생성(서버 현재시간)
        homework.setHwCreateDate(date); // 생성된 시간을 출제일에 세팅
        homework.setHwUpdateDate(date); // 생성된 시간을 수정일에 세팅 (추후 update 메서드에서는 수정일만 세팅해야)



        // 8. 과제제출파일첨부유무 세팅
        // homework_file(첨부파일) 테이블을 뒤져서 HW_id가 일치하는 파일이 존재하면 Y, 아니면 N으로 세팅
        // 아직 미구현(DB상에 테이블 없음)

        // 9. 유저_고유번호 세팅
        // principal에서 username을 얻어온 후, userDetailService로 User정보 덩어리 받아옴
        // 받아온 User를 user에 넣어서 homework에 세팅
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        homework.setUser(user);

        // 10. 제약이름 세팅
        // 제약이름이 "homework_constraint" 인 것을 찾아서 세팅
        ConstraintDefine constraintDefine = constraintDefineService.loadConstraintDefineByConstraintName("homework_constraint");
        homework.setConstraintDefine(constraintDefine);

        // 11. 반_고유번호 세팅
        // 목표1: user정보를 기반으로 ClassId를 얻어낸다
        // 목표2: 얻어낸 classId로 class정보 받아서 세팅

        // 11-1. user정보로 userId 추출
        Long userId = user.getUserId();

        // 11-2. 얻은 userId로 teacher테이블에서 teacherId 추출
        Long teacherId = teacherService.loadTeacherIdByUserId(userId);
        System.out.println("강사번호: " + teacherId);

        // 11-3. 얻은 teacherId로 class_teacher_log테이블에서 classId 추출
        Long classId = classTeacherLogService.loadClassIdByTeacherId(teacherId);
        System.out.println("클래스ID : " + classId);

        // 11-4 classId로 classGroup테이블에서 classGroup정보를 받아다 homework에 세팅
        ClassGroup classGroup = classGroupService.laodClassGroupByClassId(classId);
        homework.setClassGroup(classGroup);


        // 완전히 모든항목이 세팅된 homework를 homeworkService에게 인자로 넘겨주면서 종료
        return homeworkService.createHomework(homework);
    }



    // Todo: 관리자 계정의 과제목록 READ, WRITE권한을 줄 것인지 생각해야
    ///////////////////////////   현재 진행중인 Homework 목록읽기(=게시판)   ///////////////////////////
    // http://localhost:8080/class/assignment/list
    @PreAuthorize("hasAnyAuthority('HW_READ')")     // 학생, 강사 가능
    @RequestMapping(
            path = "/list",
            method = RequestMethod.POST,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public ResultItems<Homework> listOf(
            Principal principal,
            @RequestParam(name = "page", defaultValue = "1", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {

        // 1. principal에서 classId받아오기
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        Long userId = user.getUserId();
        Long classId = userIdToClassIdConverter.userIdToClassId(userId);

        // 1-1. 학생인 경우
        if(user.getRole().getRoleCode().equals("role_student")){
            System.out.println("현재 로그인한 사용자(userId: " + userId + ")는 학생이고, 반번호는 : "+ classId + "입니다");
        }
        // 1-2. 선생인 경우
        else if(user.getRole().getRoleCode().equals("role_teacher")) {
            System.out.println("현재 로그인한 사용자(userId: " + userId + ")는 강사이고, 반번호는 : "+ classId + "입니다");
        }
        // 1-3. 그외의 경우
        else {
            // 반정보가 없는 권한없는 사용자(=차단해야)
            System.out.println("과제목록을 볼 권한이 없는 사용자(userId: " + userId + ")가 접근했습니다");
        }

        // 2. 현재시간(=요청시간) 계산용 파트(진행중 과제만 표시하기 위해)
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String requestedTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
        System.out.println("현재시간(요청시간) : " + requestedTime);

        // 3. Pageable 파트
//        Pageable pageable = PageRequest.of(page - 1, size);
        Pageable pageable = PageRequest.of((page < 1? 0 : page-1),(size<0?10:size), Sort.by("hw_id").descending());

        // 4. homeworkService에 반번호, 현재시간 넘겨주고 조건에 부합하는(반번호 일치, 마감이 현재시간 이후) 과제들만 받아옴
        Page<Homework> futureHomeworkList = homeworkService.listOfHomeworkByClassIdAndDate(classId, requestedTime, pageable);
        System.out.println("사이즈 of futureHomeworkList: " + futureHomeworkList.getTotalElements());

        if(futureHomeworkList.getTotalElements() > 0) {
            return new ResultItems<Homework>(futureHomeworkList.stream().collect(Collectors.toList()), page, size, futureHomeworkList.getTotalElements());
        }
        else {
            // 결과가 없는 경우 에러메시지 출력해야 ("현재 진행중인 과제가 없습니다")
            // 어떻게 구현하지?
            return null;
        }
    }

    
    ///////////////////////////   종료된 Homework 목록읽기   ///////////////////////////
    // http://localhost:8080/class/assignment/listEnded
    @PreAuthorize("hasAnyAuthority('HW_READ')")     // 학생, 강사 가능
    @RequestMapping(
            path = "/listended",
            method = RequestMethod.POST,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public ResultItems<Homework> listOfEnded(
            Principal principal,
            @RequestParam(name = "page", defaultValue = "1", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {

        // 1. principal에서 classId받아오기
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        Long userId = user.getUserId();
        Long classId = userIdToClassIdConverter.userIdToClassId(userId);

        // 1-1. 학생인 경우
        if(user.getRole().getRoleCode().equals("role_student")){
            System.out.println("현재 로그인한 사용자(userId: " + userId + ")는 학생이고, 반번호는 : "+ classId + "입니다");
        }
        // 1-2. 선생인 경우
        else if(user.getRole().getRoleCode().equals("role_teacher")) {
            System.out.println("현재 로그인한 사용자(userId: " + userId + ")는 강사이고, 반번호는 : "+ classId + "입니다");
        }
        // 1-3. 그외의 경우
        else {
            // 반정보가 없는 권한없는 사용자(=차단해야)
            System.out.println("과제목록을 볼 권한이 없는 사용자(userId: " + userId + ")가 접근했습니다");
        }

        // 2. 현재시간(=요청시간) 계산용 파트(진행중 과제만 표시하기 위해)
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String requestedTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
        System.out.println("현재시간(요청시간) : " + requestedTime);

        // 3. Pageable 파트
        Pageable pageable = PageRequest.of(page - 1, size);

        // 4. homeworkService에 반번호, 현재시간 넘겨주고 조건에 부합하는(반번호 일치, 마감이 현재시간 이후) 과제들만 받아옴
        Page<Homework> EndedHomeworkList = homeworkService.listOfEndedHomeworkByClassIdAndDate(classId, requestedTime, pageable);

        if(EndedHomeworkList.getTotalElements() > 0) {
            return new ResultItems<Homework>(EndedHomeworkList.stream().collect(Collectors.toList()), page, size, EndedHomeworkList.getTotalElements());
        }
        else {
            // 결과가 없는 경우 에러메시지 출력해야 ("현재 진행중인 과제가 없습니다")
            // 어떻게 구현하지?
            return null;
        }
    }













    // Todo: 권한체크 강화해야 (list에서 한 번 거르지만 여기서 다시 걸러야함. 반이 일치하는지)
    ///////////////////////////   Homework 상세보기(=게시물 상세)   ///////////////////////////
    // http://localhost:8080/class/assignment/view?hwno=1
    @PreAuthorize("hasAnyAuthority('HW_READ')")
    @RequestMapping(
            path = "/view",
            method = RequestMethod.GET,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public Homework retrieve(
            Principal principal, @RequestParam(name = "hwno", required = true) Long hwId) {
        System.out.println("번호 : " + hwId);
        return homeworkService.itemOfHomework(hwId).get();
    }

    // Todo: 관리자는 작성자가 아니어도 수정 가능하게 해야하는지?(현재는 불가능)
    ///////////////////////////   Homework 수정 (강사만)   ///////////////////////////
    // http://localhost:8080/class/assignment/view?hwno={hwno}
    // UPDATE는 int로 반환한다 (0: 실패, 1: 성공)
    @PreAuthorize("hasAnyAuthority('HW_WRITE')")
    @RequestMapping(
            path = "/view",
            method = RequestMethod.PATCH,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public String update(
            Principal principal,
            @RequestBody Homework homework,
            @RequestParam(name = "hwno", defaultValue = "1", required = true) Long hwId
    ) {
        // 권한 검증(최초 작성자와 요청자가 동일한 경우에만 수정 가능)
        // 1. principal로 사용자의 userId 받아오기
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        Long userIdOfCurrentUser = user.getUserId();

        // 2. hwId로 homework 작성자의 userId 받아오기
        Long userIdOfHomework = homeworkService.loadHomeworkByHwId(hwId).getUser().getUserId();

        // 3. 현재 요청인과 최초 작성자가 동일인인지 검증
        // 3-1. 동일한 경우 업데이트 수행
        if (userIdOfCurrentUser == userIdOfHomework) {
            System.out.println("작성자 신원 일치 확인");
            // 업데이트 수행, 성공여부 0 or 1로 받아옴
            int isUpdateSuccess = homeworkService.updateHomework(hwId, homework);
            System.out.println("DeadLine >>>>>>>>>>>>> " + homework.getHwDeadLine());
            if (isUpdateSuccess == 0)
                return "수정에 실패했습니다";
            else if (isUpdateSuccess == 1)
                return "성공적으로 수정하였습니다";
            else
                return "알 수 없는 오류";
        }
        // 3-2. 수정하려는 사람의 userId와 최초 작성자의 userId가 상이한경우 에러출력
        else {
            System.out.println("에러: 유저번호 " + userIdOfCurrentUser + "번 유저가 타인(" + userIdOfHomework + "번 유저)이 출제한 과제를 수정 시도하였습니다");
            return "userId 불일치 경고: 본인이 제출한 과제만 수정할 수 있습니다";
        }
    }

    // Todo: 관리자는 작성자가 아니어도 삭제 가능하게 해야하는지?(현재는 불가능)
    ///////////////////////////   Homework 삭제 (강사만)   ///////////////////////////
    // http://localhost:8080/class/assignment/view?hwno=1

    @PreAuthorize("hasAnyAuthority('HW_WRITE')")
    @RequestMapping(
            path = "/view",
            method = RequestMethod.DELETE,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public String delete(
            Principal principal,
            @RequestParam(name = "hwno", defaultValue = "1", required = true) Long hwId
    ) {
        // 권한 검증(최초 작성자와 요청자가 동일한 경우에만 수정 가능)
        // 1. principal로 사용자의 userId 받아오기
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        Long userIdOfCurrentUser = user.getUserId();

        // 2. hwId로 homework 작성자의 userId 받아오기
        Long userIdOfHomework = homeworkService.loadHomeworkByHwId(hwId).getUser().getUserId();

        // 3. 현재 요청인과 최초 작성자가 동일인인지 검증
        // 3-1. 동일한 경우 삭제 수행
        if (userIdOfCurrentUser == userIdOfHomework) {
            System.out.println("작성자 신원 일치 확인");

            // 서비스에 삭제요청 (반환형 없음)
            homeworkService.deleteHomework(hwId);

            // 삭제 후 해당 hwId에 해당하는 항목이 DB에 남아있는지 확인(잘 지워졌는지)
            if(homeworkService.itemOfHomework(hwId).isPresent()==false){
                return hwId + "번 과제가 성공적으로 삭제됐습니다";
            }
            else {
                return "과제 삭제에 실패했습니다";
            }
        }
        // 3-2. 삭제하려는 사람의 userId와 최초 작성자의 userId가 상이한경우 에러출력
        else {
            System.out.println("에러: 유저번호 " + userIdOfCurrentUser + "번 유저가 타인(" + userIdOfHomework + "번 유저)이 출제한 과제를 삭제 시도하였습니다");
            return "userId 불일치 경고: 본인이 제출한 과제만 삭제할 수 있습니다";
        }
    }








}
