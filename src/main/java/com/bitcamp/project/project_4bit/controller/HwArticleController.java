///*
//create      http://localhost:8080/class/assignment/submit/write?hwno={hwno}                 POST    과제 제출물 생성
//retrieve    http://localhost:8080/class/assignment/submit/view?hwArticleId={hwArticleId}    POST    과제 제출물 개별조회
//update      http://localhost:8080/class/assignment/submit/view?hwArticleId={hwArticleId}    PATCH   과제 제출물 수정
//delete      http://localhost:8080/class/assignment/submit/view?hwArticleId={hwArticleId}    DELETE  과제 제출물 삭제
// */
//
//package com.bitcamp.project.project_4bit.controller;
//
//import com.bitcamp.project.project_4bit.entity.Homework;
//import com.bitcamp.project.project_4bit.entity.HwArticle;
//import com.bitcamp.project.project_4bit.entity.PointLog;
//import com.bitcamp.project.project_4bit.entity.User;
//import com.bitcamp.project.project_4bit.service.HomeworkService;
//import com.bitcamp.project.project_4bit.service.HwArticleService;
//import com.bitcamp.project.project_4bit.service.LocalUserDetailsService;
//import com.bitcamp.project.project_4bit.service.PointLogService;
//import com.bitcamp.project.project_4bit.util.UserIdToClassIdConverter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.security.Principal;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/class/assignment/submit")
//public class HwArticleController {
//
//    @Autowired
//    private HwArticleService hwArticleService;
//
//    @Autowired
//    private HomeworkService homeworkService;
//
//    @Autowired
//    private LocalUserDetailsService userDetailsService;
//
//    @Autowired
//    private UserIdToClassIdConverter userIdToClassIdConverter;
//
//    @Autowired
//    private PointLogService pointLogService;
//
//
//    // Todo: 학생 아니어도 가능?, 현재 유효한 과제인가 검증 필요
//    ///////////////////////////   HwArticle 글쓰기(=과제제출)   ///////////////////////////
//    // http://localhost:8080/class/assignment/submit/write?hwno={hwno}
//    @PreAuthorize("hasAnyAuthority('SHW_WRITE')")    // 학생만 가능
//    @RequestMapping(
//            path = "/write",
//            method = RequestMethod.POST,
//            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
//    public HwArticle create(Principal principal,
//                            @RequestBody HwArticle hwArticle,
//                            @RequestParam(name = "hwno", required = true) Long hwId) {
//
//        //////////// param으로 넘어오는 HwArticle 구성 //////////////////////////////
////        1. HW_article_id : 사용자입력X, 자동부여
////        2. HW_submit_date : 사용자입력X, 컨트롤러에서 부여해야
////        3. HW_update_date : 사용자입력X, 컨트롤러에서 부여해야
////        4. HW_contents : 사용자 입력
////        5. HW_isFile : 사용자입력X, 컨트롤러에서 부여해야
////        6. HW_id : 사용자입력X, 주소줄에서 찾아서 지정
////        7. user_id : 사용자입력X, principal에서 찾아서 지정
//        ///////////////////////////////////////////////////////////////////////////////
//
//        // 2. HW_submit_date & 3. HW_update_date 세팅
//        Date date = new Date();
//        hwArticle.setHwSubmitDate(date);
//        hwArticle.setHwUpdateDate(date);
//
//        // 5. HW_isFile 세팅 (DB가 구조적으로 문제. 그것부터 해결해야)
//
//
//        // 6. HW_id 세팅
//        Homework homework = homeworkService.loadHomeworkByHwId(hwId);
//        hwArticle.setHomework(homework);
//
//        // 7. user_id 세팅
//        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
//        hwArticle.setUser(user);
//
//        System.out.println("들어온 hwContents: " + hwArticle.getHwContents());
//
//        // 세팅 완료된 hwArticle 덩어리를 hwArticleService를 통해 저장(=create)
//        return hwArticleService.createHwArticle(hwArticle);
//    }
//
//
//    // Todo : 관리자도 상세보기가 가능하게 해야하나?
//    ///////////////////////////   HwArticle 상세보기(=제출한 과제 확인)   ///////////////////////////
//    // http://localhost:8080/class/assignment/submit/view?hwArticleId={hwArticleId}
//    @PreAuthorize("hasAnyAuthority('SHW_READ')")
//    @RequestMapping(
//            path = "/view",
//            method = RequestMethod.POST,
//            produces = {
//                    MediaType.APPLICATION_JSON_UTF8_VALUE,
//                    MediaType.APPLICATION_XML_VALUE
//            }
//    )
//    public HwArticle retrieve(Principal principal,
//                              @RequestParam(name = "hwArticleId", required = true) Long hwArticleId
//    ) {
//
//        // 권한 검증 필요
//        // 학생은 자기자신이 제출한 과제물만, 강사는 자기반의 학생들이 제출한 모든 과제를 볼 수 있어야 함
//
//        // 1-1. principal로 사용자의 userId 및 classId 받아오기
//        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
//        Long userIdOfCurrentUser = user.getUserId();
//        Long classIdOfCurrentUser = userIdToClassIdConverter.userIdToClassId(userIdOfCurrentUser);
//
//        // 1-2. hwArticleId로 hwArticle 작성자의 userId와 classId 받아오기
//        Long userIdOfHwArticle = hwArticleService.loadUserIdByHwArticleId(hwArticleId);
//        Long classIdOfHwArticle = hwArticleService.loadClassIdByHwArticleId(hwArticleId);
//
//        // 2-1. 학생인 경우
//        if (user.getRole().getRoleCode().equals("role_student")) {
//            // 현재 접근하려는 사람의 userId가 기존에 과제 제출한 사람의 userId와 동일한 경우에만 열람 허용
//            if (userIdOfCurrentUser == userIdOfHwArticle) {
//                System.out.println("학생 신원 조회 성공");
//                return hwArticleService.itemOfHwArticle(hwArticleId).get();
//            } else {
//                // userId 불일치 경고: 본인의 과제 제출물만 볼 수 있습니다
//                System.out.println("에러: 유저번호 " + userIdOfCurrentUser + "번 유저(학생)가 타인의 제출물을 열람 시도하였습니다");
//                return null;
//            }
//        }
//
//        // 2-2. 강사인 경우
//        else if (user.getRole().getRoleCode().equals("role_teacher")) {
//            // 본인이 맡은 반 학생의 과제만 열람 가능(= 열람 요청자와 최초 작성자의 classId 일치여부 확인)
//
//            if (classIdOfCurrentUser == classIdOfHwArticle) {
//                System.out.println("강사 신원 조회 성공");
//                return hwArticleService.itemOfHwArticle(hwArticleId).get();
//            } else {
//                // classId 불일치 경고: 강사는 자신의 담당반 학생들의 과제 제출물만 볼 수 있습니다
//                System.out.println("유저번호 " + userIdOfCurrentUser + "번 유저(강사)가 타반 학생의 제출물을 열람 시도하였습니다");
//                return null;
//            }
//        }
//
//        // 2-3. 그외의 경우(Admin 외)
//        else {
//            // 제출한 과제를 볼 권한이 없는 사용자(=차단해야)
//            System.out.println("제출 과제를 볼 권한이 없는 사용자(유저번호" + userIdOfCurrentUser + "번)가 열람을 시도했습니다");
//            return null;
//        }
//    }
//
//
//    // Todo : 관리자는 본인이 쓴게 아니어도 수정이 가능하게 해야하나?
//    ///////////////////////////   HwArticle 수정 (학생만)   ///////////////////////////
//    // http://localhost:8080/class/assignment/submit/view?hwArticleId={hwArticleId}
//    @PreAuthorize("hasAnyAuthority('SHW_WRITE')")   // 학생만 가능
//    @RequestMapping(
//            path = "/view",
//            method = RequestMethod.PATCH,
//            produces = {
//                    MediaType.APPLICATION_JSON_UTF8_VALUE,
//                    MediaType.APPLICATION_XML_VALUE
//            }
//    )
//    public String update(
//            Principal principal,
//            @RequestBody HwArticle hwArticle,
//            @RequestParam(name = "hwArticleId", required = true) Long hwArticleId
//    ) {
//        // 권한 검증(학생이면서 최초 작성자와 동일한 경우에만 가능)
//        // 1-1. principal로 사용자의 userId 받아오기
//        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
//        Long userIdOfCurrentUser = user.getUserId();
//
//        // 1-2. hwArticleId로 hwArticle 작성자의 userId 받아오기
//        Long userIdOfHwArticle = hwArticleService.loadUserIdByHwArticleId(hwArticleId);
//
//        // 2-1. 학생인 경우
//        if (user.getRole().getRoleCode().equals("role_student")) {
//            // 현재 접근하려는 사람의 userId가 기존에 과제 제출한 사람의 userId와 동일한 경우에만 수정 허용
//            if (userIdOfCurrentUser == userIdOfHwArticle) {
//                System.out.println("작성자 신원 일치 확인");
//                // 업데이트 수행, 성공여부 0 or 1로 받아옴
//                int isUpdateSuccess = hwArticleService.updateHwArticle(hwArticleId, hwArticle);
//
//                if (isUpdateSuccess == 0)
//                    return "수정에 실패했습니다";
//                else if (isUpdateSuccess == 1)
//                    return "성공적으로 수정하였습니다";
//                else
//                    return "알 수 없는 오류";
//            } else {
//                // userId 불일치 경고: 본인의 과제 제출물만 볼 수 있습니다
//                System.out.println("에러: 유저번호 " + userIdOfCurrentUser + "번 유저(학생)가 타인(" + userIdOfHwArticle + "번 유저)의 제출물을 수정 시도하였습니다");
//                return "userId 불일치 경고: 본인이 제출한 과제만 수정할 수 있습니다";
//            }
//        } else {
//            // 신원 불일치 경고: 학생이 아니면 수정메뉴 진입 불가능
//            System.out.println("에러: 유저번호 " + userIdOfCurrentUser + "번 유저(비학생)가 학생(" + userIdOfHwArticle + "번 유저)의 제출물을 수정 시도하였습니다");
//            return "신원 불일치 경고: 제출한 과제 수정은 학생만 가능합니다";
//        }
//    }
//
//
//    // Todo : 관리자는 본인이 쓴게 아니어도 삭제가 가능하게 해야하나?
//    ///////////////////////////   HwArticle 삭제 (학생만)   ///////////////////////////
//    // http://localhost:8080/class/assignment/submit/view?hwArticleId={hwArticleId}
//    @PreAuthorize("hasAnyAuthority('SHW_WRITE')")   // 학생만 가능
//    @RequestMapping(
//            path = "/view",
//            method = RequestMethod.DELETE,
//            produces = {
//                    MediaType.APPLICATION_JSON_UTF8_VALUE,
//                    MediaType.APPLICATION_XML_VALUE
//            }
//    )
//    public String delete(Principal principal,
//                         @RequestParam(name = "hwArticleId", required = true) Long hwArticleId) {
//
//        // 권한 검증(학생이면서 최초 작성자와 동일한 경우에만 가능)
//        // 1-1. principal로 사용자의 userId 받아오기
//        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
//        Long userIdOfCurrentUser = user.getUserId();
//
//        // 1-2. hwArticleId로 hwArticle 작성자의 userId 받아오기
//        Long userIdOfHwArticle = hwArticleService.loadUserIdByHwArticleId(hwArticleId);
//
//        // 2-1. 학생인 경우
//        if (user.getRole().getRoleCode().equals("role_student")) {
//            // 현재 접근하려는 사람의 userId가 기존에 과제 제출한 사람의 userId와 동일한 경우에만 수정 허용
//            if (userIdOfCurrentUser == userIdOfHwArticle) {
//                System.out.println("학생 신원 조회 성공");
//                // 삭제 수행, 리턴없음(void)
//                hwArticleService.deleteHwArticle(hwArticleId);
//
//                // 삭제 후 해당 hwId에 해당하는 항목이 DB에 남아있는지 확인(잘 지워졌는지)
//                if (hwArticleService.itemOfHwArticle(hwArticleId).isPresent() == false) {
//                    return hwArticleId + "번 과제 제출물이 성공적으로 삭제됐습니다";
//                } else {
//                    return "과제 제출물 삭제에 실패했습니다";
//                }
//            } else {
//                // userId 불일치 경고: 본인의 과제 제출물만 삭제할 수 있습니다
//                System.out.println("에러: 유저번호 " + userIdOfCurrentUser + "번 유저(학생)가 타인(" + userIdOfHwArticle + "번 유저)의 제출물을 삭제 시도하였습니다");
//                return "userId 불일치 경고: 본인이 제출한 과제만 삭제할 수 있습니다";
//            }
//        } else {
//            // 신원 불일치 경고: 학생이 아니면 수정메뉴 진입 불가능
//            System.out.println("에러: 유저번호 " + userIdOfCurrentUser + "번 유저(비학생)가 학생(" + userIdOfHwArticle + "번 유저)의 제출물을 삭제 시도하였습니다");
//            return "신원 불일치 경고: 제출한 과제 삭제는 학생만 가능합니다";
//        }
//    }
//
//
//    ///////////////////////////   자신이 작성한 HwArticle 조회(학생용)   ///////////////////////////
//    // http://localhost:8080/class/assignment/submit/findMyHwArticle?hwId={hwId}
//
//    //    @PreAuthorize("hasAnyAuthority('HW_READ')")
//    @RequestMapping(
//            path = "/findMyHwArticle",
//            method = RequestMethod.POST,
//            produces = {
//                    MediaType.APPLICATION_JSON_UTF8_VALUE,
//                    MediaType.APPLICATION_XML_VALUE
//            }
//    )
//    public Map<Boolean,Long> findMyHwArticle(Principal principal, @RequestParam(name = "hwId", required = true) Long hwId) {
//        // 반환 형태가 있으면 Long  없으면 boolean형으로 다름
//        // 없을 때 프론트에서 Long을 받아야 하는데 boolean형으로 들어오니까 임시로 값을 넣어주는 것 같다
//        // boolean으로 지정 시 >> 상세보기 불가
//        // Long으로 지정 시 >> 리액트에서 Long을 비교할 수 없음
//        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
//        Long userIdOfCurrentUser = user.getUserId();
//        System.out.println("접속한 유저: " + user.getName());
//        System.out.println("찾아낸 hwArticleId: " + hwArticleService.loadHwArticleIdByHwIdAndUserId(hwId, userIdOfCurrentUser));
//
//        Long hwArticleId = hwArticleService.loadHwArticleIdByHwIdAndUserId(hwId, userIdOfCurrentUser);
//        Map<Boolean, Long> resultMap = new HashMap<>();
//        if (hwArticleId != null) {
//            resultMap.put(true, hwArticleId);
//            return  resultMap;
//        } else {
//            resultMap.put(false, 0L);
//            return  resultMap;
//        }
//    }
//
//
//
//
//
//    ///////////////////////////   학생이 작성한 HwArticle 조회(강사용)   ///////////////////////////
//    // http://localhost:8080/class/assignment/submit/findMyHwArticle?hwId={hwId}
//
//    //    @PreAuthorize("hasAnyAuthority('HW_READ')")
//    @RequestMapping(
//            path = "/findMyHwArticleForTeacher",
//            method = RequestMethod.POST,
//            produces = {
//                    MediaType.APPLICATION_JSON_UTF8_VALUE,
//                    MediaType.APPLICATION_XML_VALUE
//            }
//    )
//    public Map<Boolean, Long> findMyHwArticleForTeacher(Principal principal, @RequestParam(name = "hwId", required = true) Long hwId, @RequestParam(name = "userId", required = true) Long userId) {
//
//        System.out.println("찾아낸 hwArticleId: " + hwArticleService.loadHwArticleIdByHwIdAndUserId(hwId, userId));
//
//        Long hwArticleId = hwArticleService.loadHwArticleIdByHwIdAndUserId(hwId, userId);
//        Map<Boolean, Long> resultMap = new HashMap<>();
//        if (hwArticleId != null) {
//            resultMap.put(true, hwArticleId);
//            return  resultMap;
//        } else {
//            resultMap.put(false, 0L);
//            return  resultMap;
//        }
//    }
//
//}

/*
create      http://localhost:8080/class/assignment/submit/write?hwno={hwno}                 POST    과제 제출물 생성
retrieve    http://localhost:8080/class/assignment/submit/view?hwArticleId={hwArticleId}    POST    과제 제출물 개별조회
update      http://localhost:8080/class/assignment/submit/view?hwArticleId={hwArticleId}    PATCH   과제 제출물 수정
delete      http://localhost:8080/class/assignment/submit/view?hwArticleId={hwArticleId}    DELETE  과제 제출물 삭제
 */

        package com.bitcamp.project.project_4bit.controller;

import com.bitcamp.project.project_4bit.entity.Homework;
import com.bitcamp.project.project_4bit.entity.HwArticle;
import com.bitcamp.project.project_4bit.entity.PointLog;
import com.bitcamp.project.project_4bit.entity.User;
import com.bitcamp.project.project_4bit.service.HomeworkService;
import com.bitcamp.project.project_4bit.service.HwArticleService;
import com.bitcamp.project.project_4bit.service.LocalUserDetailsService;
import com.bitcamp.project.project_4bit.service.PointLogService;
import com.bitcamp.project.project_4bit.util.UserIdToClassIdConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;

@RestController
@RequestMapping("/class/assignment/submit")
public class HwArticleController {

    @Autowired
    private HwArticleService hwArticleService;

    @Autowired
    private HomeworkService homeworkService;

    @Autowired
    private LocalUserDetailsService userDetailsService;

    @Autowired
    private UserIdToClassIdConverter userIdToClassIdConverter;

    @Autowired
    private PointLogService pointLogService;




    // Todo: 학생 아니어도 가능?, 현재 유효한 과제인가 검증 필요
    ///////////////////////////   HwArticle 글쓰기(=과제제출)   ///////////////////////////
    // http://localhost:8080/class/assignment/submit/write?hwno={hwno}
    @PreAuthorize("hasAnyAuthority('SHW_WRITE')")    // 학생만 가능
    @RequestMapping(
            path = "/write",
            method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public HwArticle create(Principal principal,
                            @RequestBody HwArticle hwArticle,
                            @RequestParam(name = "hwno", required = true) Long hwId) {

        //////////// param으로 넘어오는 HwArticle 구성 //////////////////////////////
//        1. HW_article_id : 사용자입력X, 자동부여
//        2. HW_submit_date : 사용자입력X, 컨트롤러에서 부여해야
//        3. HW_update_date : 사용자입력X, 컨트롤러에서 부여해야
//        4. HW_contents : 사용자 입력
//        5. HW_isFile : 사용자입력X, 컨트롤러에서 부여해야
//        6. HW_id : 사용자입력X, 주소줄에서 찾아서 지정
//        7. user_id : 사용자입력X, principal에서 찾아서 지정
        ///////////////////////////////////////////////////////////////////////////////

        // 2. HW_submit_date & 3. HW_update_date 세팅
        Date date = new Date();
        hwArticle.setHwSubmitDate(date);
        hwArticle.setHwUpdateDate(date);

        // 5. HW_isFile 세팅 (DB가 구조적으로 문제. 그것부터 해결해야)


        // 6. HW_id 세팅
        Homework homework = homeworkService.loadHomeworkByHwId(hwId);
        hwArticle.setHomework(homework);

        // 7. user_id 세팅
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        hwArticle.setUser(user);

        System.out.println("들어온 hwContents: " + hwArticle.getHwContents());

        // 세팅 완료된 hwArticle 덩어리를 hwArticleService를 통해 저장(=create)
        return hwArticleService.createHwArticle(hwArticle);
    }




    // Todo : 관리자도 상세보기가 가능하게 해야하나?
    ///////////////////////////   HwArticle 상세보기(=제출한 과제 확인)   ///////////////////////////
    // http://localhost:8080/class/assignment/submit/view?hwArticleId={hwArticleId}
    @PreAuthorize("hasAnyAuthority('SHW_READ')")
    @RequestMapping(
            path = "/view",
            method = RequestMethod.POST,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public HwArticle retrieve(Principal principal,
                              @RequestParam(name = "hwArticleId", required = true) Long hwArticleId
    ) {

        // 권한 검증 필요
        // 학생은 자기자신이 제출한 과제물만, 강사는 자기반의 학생들이 제출한 모든 과제를 볼 수 있어야 함

        // 1-1. principal로 사용자의 userId 및 classId 받아오기
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        Long userIdOfCurrentUser = user.getUserId();
        Long classIdOfCurrentUser = userIdToClassIdConverter.userIdToClassId(userIdOfCurrentUser);

        // 1-2. hwArticleId로 hwArticle 작성자의 userId와 classId 받아오기
        Long userIdOfHwArticle = hwArticleService.loadUserIdByHwArticleId(hwArticleId);
        Long classIdOfHwArticle = hwArticleService.loadClassIdByHwArticleId(hwArticleId);

        // 2-1. 학생인 경우
        if(user.getRole().getRoleCode().equals("role_student")) {
            // 현재 접근하려는 사람의 userId가 기존에 과제 제출한 사람의 userId와 동일한 경우에만 열람 허용
            if(userIdOfCurrentUser == userIdOfHwArticle){
                System.out.println("학생 신원 조회 성공");
                return hwArticleService.itemOfHwArticle(hwArticleId).get();
            }
            else {
                // userId 불일치 경고: 본인의 과제 제출물만 볼 수 있습니다
                System.out.println("에러: 유저번호 " + userIdOfCurrentUser + "번 유저(학생)가 타인의 제출물을 열람 시도하였습니다");
                return null;
            }
        }

        // 2-2. 강사인 경우
        else if(user.getRole().getRoleCode().equals("role_teacher")) {
            // 본인이 맡은 반 학생의 과제만 열람 가능(= 열람 요청자와 최초 작성자의 classId 일치여부 확인)

            if(classIdOfCurrentUser == classIdOfHwArticle) {
                System.out.println("강사 신원 조회 성공");
                return hwArticleService.itemOfHwArticle(hwArticleId).get();
            }
            else {
                // classId 불일치 경고: 강사는 자신의 담당반 학생들의 과제 제출물만 볼 수 있습니다
                System.out.println("유저번호 " + userIdOfCurrentUser + "번 유저(강사)가 타반 학생의 제출물을 열람 시도하였습니다");
                return null;
            }
        }

        // 2-3. 그외의 경우(Admin 외)
        else {
            // 제출한 과제를 볼 권한이 없는 사용자(=차단해야)
            System.out.println("제출 과제를 볼 권한이 없는 사용자(유저번호" + userIdOfCurrentUser + "번)가 열람을 시도했습니다");
            return null;
        }
    }



    // Todo : 관리자는 본인이 쓴게 아니어도 수정이 가능하게 해야하나?
    ///////////////////////////   HwArticle 수정 (학생만)   ///////////////////////////
    // http://localhost:8080/class/assignment/submit/view?hwArticleId={hwArticleId}
    @PreAuthorize("hasAnyAuthority('SHW_WRITE')")   // 학생만 가능
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
            @RequestBody HwArticle hwArticle,
            @RequestParam(name = "hwArticleId", required = true) Long hwArticleId
    ) {
        // 권한 검증(학생이면서 최초 작성자와 동일한 경우에만 가능)
        // 1-1. principal로 사용자의 userId 받아오기
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        Long userIdOfCurrentUser = user.getUserId();

        // 1-2. hwArticleId로 hwArticle 작성자의 userId 받아오기
        Long userIdOfHwArticle = hwArticleService.loadUserIdByHwArticleId(hwArticleId);

        // 2-1. 학생인 경우
        if(user.getRole().getRoleCode().equals("role_student")) {
            // 현재 접근하려는 사람의 userId가 기존에 과제 제출한 사람의 userId와 동일한 경우에만 수정 허용
            if(userIdOfCurrentUser == userIdOfHwArticle){
                System.out.println("작성자 신원 일치 확인");
                // 업데이트 수행, 성공여부 0 or 1로 받아옴
                int isUpdateSuccess = hwArticleService.updateHwArticle(hwArticleId, hwArticle);

                if(isUpdateSuccess==0)
                    return "수정에 실패했습니다";
                else if(isUpdateSuccess==1)
                    return "성공적으로 수정하였습니다";
                else
                    return "알 수 없는 오류";
            }
            else {
                // userId 불일치 경고: 본인의 과제 제출물만 볼 수 있습니다
                System.out.println("에러: 유저번호 " + userIdOfCurrentUser + "번 유저(학생)가 타인(" + userIdOfHwArticle + "번 유저)의 제출물을 수정 시도하였습니다");
                return "userId 불일치 경고: 본인이 제출한 과제만 수정할 수 있습니다";
            }
        }
        else {
            // 신원 불일치 경고: 학생이 아니면 수정메뉴 진입 불가능
            System.out.println("에러: 유저번호 " + userIdOfCurrentUser + "번 유저(비학생)가 학생(" + userIdOfHwArticle + "번 유저)의 제출물을 수정 시도하였습니다");
            return "신원 불일치 경고: 제출한 과제 수정은 학생만 가능합니다";
        }
    }



    // Todo : 관리자는 본인이 쓴게 아니어도 삭제가 가능하게 해야하나?
    ///////////////////////////   HwArticle 삭제 (학생만)   ///////////////////////////
    // http://localhost:8080/class/assignment/submit/view?hwArticleId={hwArticleId}
    @PreAuthorize("hasAnyAuthority('SHW_WRITE')")   // 학생만 가능
    @RequestMapping(
            path = "/view",
            method = RequestMethod.DELETE,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public String delete(Principal principal,
                         @RequestParam(name = "hwArticleId", required = true) Long hwArticleId) {

        // 권한 검증(학생이면서 최초 작성자와 동일한 경우에만 가능)
        // 1-1. principal로 사용자의 userId 받아오기
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        Long userIdOfCurrentUser = user.getUserId();

        // 1-2. hwArticleId로 hwArticle 작성자의 userId 받아오기
        Long userIdOfHwArticle = hwArticleService.loadUserIdByHwArticleId(hwArticleId);

        // 2-1. 학생인 경우
        if(user.getRole().getRoleCode().equals("role_student")) {
            // 현재 접근하려는 사람의 userId가 기존에 과제 제출한 사람의 userId와 동일한 경우에만 수정 허용
            if(userIdOfCurrentUser == userIdOfHwArticle){
                System.out.println("학생 신원 조회 성공");
                // 삭제 수행, 리턴없음(void)
                hwArticleService.deleteHwArticle(hwArticleId);

                // 삭제 후 해당 hwId에 해당하는 항목이 DB에 남아있는지 확인(잘 지워졌는지)
                if(hwArticleService.itemOfHwArticle(hwArticleId).isPresent()==false){
                    return hwArticleId + "번 과제 제출물이 성공적으로 삭제됐습니다";
                }
                else {
                    return "과제 제출물 삭제에 실패했습니다";
                }
            }
            else {
                // userId 불일치 경고: 본인의 과제 제출물만 삭제할 수 있습니다
                System.out.println("에러: 유저번호 " + userIdOfCurrentUser + "번 유저(학생)가 타인(" + userIdOfHwArticle + "번 유저)의 제출물을 삭제 시도하였습니다");
                return "userId 불일치 경고: 본인이 제출한 과제만 삭제할 수 있습니다";
            }
        }
        else {
            // 신원 불일치 경고: 학생이 아니면 수정메뉴 진입 불가능
            System.out.println("에러: 유저번호 " + userIdOfCurrentUser + "번 유저(비학생)가 학생(" + userIdOfHwArticle + "번 유저)의 제출물을 삭제 시도하였습니다");
            return "신원 불일치 경고: 제출한 과제 삭제는 학생만 가능합니다";
        }
    }






    ///////////////////////////   자신이 작성한 HwArticle 조회(학생용)   ///////////////////////////
    // http://localhost:8080/class/assignment/submit/findMyHwArticle?hwId={hwId}

    //    @PreAuthorize("hasAnyAuthority('HW_READ')")
    @RequestMapping(
            path = "/findMyHwArticle",
            method = RequestMethod.POST,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public Long findMyHwArticle(Principal principal, @RequestParam(name = "hwId", required = true) Long hwId) {

        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        Long userIdOfCurrentUser = user.getUserId();
        System.out.println("접속한 유저: " + user.getName());
        System.out.println("찾아낸 hwArticleId: " + hwArticleService.loadHwArticleIdByHwIdAndUserId(hwId, userIdOfCurrentUser));

        return hwArticleService.loadHwArticleIdByHwIdAndUserId(hwId, userIdOfCurrentUser);
    }




    ///////////////////////////   학생이 작성한 HwArticle 조회(강사용)   ///////////////////////////
    // http://localhost:8080/class/assignment/submit/findMyHwArticle?hwId={hwId}

    //    @PreAuthorize("hasAnyAuthority('HW_READ')")
    @RequestMapping(
            path = "/findMyHwArticleForTeacher",
            method = RequestMethod.POST,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public Long findMyHwArticleForTeacher(Principal principal, @RequestParam(name = "hwId", required = true) Long hwId, @RequestParam(name = "userId", required = true) Long userId) {

        System.out.println("찾아낸 hwArticleId: " + hwArticleService.loadHwArticleIdByHwIdAndUserId(hwId, userId));

        return hwArticleService.loadHwArticleIdByHwIdAndUserId(hwId, userId);
    }

}
