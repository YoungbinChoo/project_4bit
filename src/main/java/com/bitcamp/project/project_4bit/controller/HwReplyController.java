/*
최상위 도메인: /hwreply

create : /hwreply/write?hwArticleId={hwArticleId}   POST    새 댓글 생성
listOf : /hwreply/list?hwArticleId={hwArticleId}    POST    댓글 목록 읽기
delete : /hwreply/list?hwArticleId={hwArticleId}&hwReplyId={hwReplyId}  DELETE  댓글 삭제
 */

package com.bitcamp.project.project_4bit.controller;

import com.bitcamp.project.project_4bit.entity.HwArticle;
import com.bitcamp.project.project_4bit.entity.HwReply;
import com.bitcamp.project.project_4bit.entity.User;
import com.bitcamp.project.project_4bit.model.ResultItems;
import com.bitcamp.project.project_4bit.service.HwArticleService;
import com.bitcamp.project.project_4bit.service.HwReplyService;
import com.bitcamp.project.project_4bit.service.LocalUserDetailsService;
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
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/hwreply")
public class HwReplyController {

    @Autowired
    private HwReplyService hwReplyService;

    @Autowired
    private LocalUserDetailsService userDetailsService;

    @Autowired
    private HwArticleService hwArticleService;

    @Autowired
    private UserIdToClassIdConverter userIdToClassIdConverter;




    ///////////////////////////   HwReply 작성(과제댓글, 학생/강사 가능)   ///////////////////////////
    // http://localhost:8080/hwreply/write?hwArticleId={hwArticleId}
    // 권한 검증이 필요한가?

    @PreAuthorize("hasAnyAuthority('SHW_WRITE')")
    @RequestMapping(
            path = "/write",
            method = RequestMethod.POST,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public HwReply create(Principal principal,
                          @RequestBody HwReply hwReply,
                          @RequestParam(name = "hwArticleId", defaultValue = "1", required = true) Long hwArticleId) {

        //////////// param으로 넘어오는 HwReply 구성 //////////////////////////////
//        1. HW_reply_id : 사용자입력X, 자동부여
//        2. HW_reply_contents : 사용자입력
//        3. HW_reply_create_date : 사용자입력X, 컨트롤러에서 부여해야
//        4. HW_reply_update_date : 사용자입력X, 컨트롤러에서 부여해야
//        5. HW_article_id : 사용자입력X, 주소줄에서 찾아서 지정
//        6. user_id : 사용자입력X, principal에서 찾아서 지정
        ///////////////////////////////////////////////////////////////////////////////

        // 3. HW_reply_create_date & 4. HW_reply_update_date 세팅
        Date date = new Date();
        hwReply.setHwReplyCreateDate(date);
        hwReply.setHwReplyUpdateDate(date);

        // 5. HW_article_id 세팅
        HwArticle hwArticle = (hwArticleService.loadHwArticleByHwArticleId(hwArticleId));
        hwReply.setHwArticle(hwArticle);

        // 6. user_id 세팅
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        hwReply.setUser(user);

        // 세팅완료된 hwArticle 덩어리를 hwReplyService를 통해 저장(=create)
        return hwReplyService.createHwReply(hwReply);

    }


    ///////////////////////////   HwReply 목록표시   ///////////////////////////
    // http://localhost:8080/hwreply/list?hwArticleId={hwArticleId}

    // 권한 검증 필요함(why? 자기가 쓴 댓글 옆에는 x표시로 지울 수 있어야 하기 때문)
    // 구현은 나중에 하더라도 권한비교 할 수 있게끔 기본은 해놔야
    // 학생/강사는 자신이 쓴 댓글만 삭제가능, 관리자는 글쓴이가 아니더라도 삭제 가능해야

    @PreAuthorize("hasAnyAuthority('SHW_READ')")
    @RequestMapping(
            path = "/list",
            method = RequestMethod.POST,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public ResultItems<HwReply> listOf(
            Principal principal,
            @RequestParam(name = "hwArticleId", defaultValue = "1", required = true) Long hwArticleId,
            @RequestParam(name = "page", defaultValue = "1", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        // 1. principal로 사용자의 userId 받아오기
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        Long userIdOfCurrentUser = user.getUserId();

        // 2. Pageable 파트
//        Pageable pageable = PageRequest.of(page - 1, size);
        Pageable pageable = PageRequest.of((page < 1? 0 : page-1),(size<0?10:size), Sort.by("id").descending());


        // 3. hwReplyService에 hwArticleId 넘겨주고 조건에 부합하는(=hwArticleId가 일치하는) 댓글만 받아옴
        Page<HwReply> hwReplyList = hwReplyService.listOfHwReplyByHwArticleId(hwArticleId, pageable);
        System.out.println("해당 hwArticle에 달린 댓글갯수: " + hwReplyList.getTotalElements() + "개");

    //        // 4. hwReplyId로 댓글 원작성자의 userId 받아오기
//        Long userIdOfHwReply = hwReplyService.loadHwReplyByHwReplyId(hwReplyId).getUser().getUserId();

        return new ResultItems<HwReply>(hwReplyList.stream().collect(Collectors.toList()), page, size, hwReplyList.getTotalElements());
    }


    // HwReply 하나 조회
    @PreAuthorize("hasAnyAuthority('NOTICE_READ','JOB_READ','PRO_READ','CBOARD_READ','CNOTICE_READ','LIBRARY_READ')")
    @RequestMapping(
            path = "/view",
            method = RequestMethod.GET,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )public HwReply retrieve(
            @RequestParam(name = "hwArticleId", required = true) Long hwArticleId){

                return hwReplyService.itemOfHwReply(hwArticleId).get();
    }



    ///////////////////////////   HwReply 삭제   ///////////////////////////
    // http://localhost:8080/hwreply/list?hwArticleId={hwArticleId}&hwReplyId={hwReplyId}

    @PreAuthorize("hasAnyAuthority('SHW_WRITE')")
    @RequestMapping(
            path = "/list",
            method = RequestMethod.DELETE,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public String delete(
            Principal principal,
            @RequestParam(name = "hwArticleId", defaultValue = "1", required = true) Long hwArticleId,
        @RequestParam(name = "hwReplyId", defaultValue = "1", required = true) Long hwReplyId
    ) {
        String message = "";

        // 권한 검증(학생/강사는 자신이 쓴 댓글만 삭제가능, 관리자는 글쓴이가 아니더라도 삭제 가능)
        // 1. principal로 사용자의 userId 받아오기
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        Long userIdOfCurrentUser = user.getUserId();

        // 2. hwReplyId로 원작성자의 userId 받아오기
        Long userIdOfHwReply = hwReplyService.loadUserIdByHwReplyId(hwReplyId);

        // 3-1. 관리자의 경우 모두 삭제허용
        if(user.getRole().getRoleCode().equals("role_admin")) {
            hwReplyService.deleteHwReply(hwReplyId);
            if(hwReplyService.itemOfHwReply(hwReplyId).isPresent()==false) {
                System.out.println("관리자(userId: " + userIdOfCurrentUser + ")가 유저(userId: " + userIdOfHwReply + ")의 댓글을 삭제했습니다");
                message = "(hwArticleId: " + hwArticleId + ")번 글에 달린 (hwReplyId: " + hwReplyId + ")번 댓글이 성공적으로 삭제됐습니다";
            }
            else {
                message = "댓글 삭제에 실패했습니다";
            }
        }
        // 3-2. 학생, 강사의 경우 자기 댓글만 삭제허용
        else if(user.getRole().getRoleCode().equals("role_student")||user.getRole().getRoleCode().equals("role_teacher")) {
            if(userIdOfCurrentUser==userIdOfHwReply) {
                hwReplyService.deleteHwReply(hwReplyId);
                if(hwReplyService.itemOfHwReply(hwReplyId).isPresent()==false) {
                    System.out.println("유저(userId: " + userIdOfHwReply + ")가 자신의 댓글을 삭제했습니다");
                    message = "(hwArticleId: " + hwArticleId + ")번 글에 달린 (hwReplyId: " + hwReplyId + ")번 댓글이 성공적으로 삭제됐습니다";
                }
                else {
                    message = "댓글 삭제에 실패했습니다";
                }
            }
            else {
                System.out.println("유저(userId: " + userIdOfCurrentUser + ")가 타인(userId: " + userIdOfHwReply + ")의 댓글을 삭제하려고 시도했습니다");
                message = "본인이 작성한 댓글만 삭제할 수 있습니다";
            }
        }
        // 3-3. 관리자/학생/강사 모두 아닌 경우 댓글 삭제 권한 없음
        else {
            message = "댓글을 삭제할 권한이 없습니다";
        }
        return message;
    }









}
