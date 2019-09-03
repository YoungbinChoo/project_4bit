package com.bitcamp.project.project_4bit.controller;

/*
작성자     : 이중호
작성일시    : 19.08.13 09:43

1. create()  댓글 생성

2. listOf    댓글들 전체출력

3. update    댓글 수정

4. delete    댓글 삭제
* */

import com.bitcamp.project.project_4bit.entity.Reply;
import com.bitcamp.project.project_4bit.entity.User;
import com.bitcamp.project.project_4bit.model.ResultItems;
import com.bitcamp.project.project_4bit.service.ArticleService;
import com.bitcamp.project.project_4bit.service.LocalUserDetailsService;
import com.bitcamp.project.project_4bit.service.ReplyService;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reply")
public class ReplyController {

    @Autowired
    private LocalUserDetailsService userDetailsService;

    @Autowired
    private ReplyService replyService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserIdToClassIdConverter userIdToClassIdConverter;


    // 댓글 작성
    // EndPoint : http://localhost:8080/reply/write?boardId=class_1_board&articleId=23
    @PreAuthorize("hasAnyAuthority('NOTICE_WRITE','JOB_WRITE','PRO_WRITE','CBOARD_WRITE','CNOTICE_WRITE','LIBRARY_WRITE')")
    @RequestMapping(
            path = "/write",
            method = RequestMethod.POST,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public Reply create(
            Principal principal,
            @RequestParam(name = "articleId", required = true) Long articleId,
            @RequestBody Reply reply) {

        // 1. 유저들의 정보(권한) 을 세팅해준다.
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        reply.setUser(user);

        // 2. 게시글(article)의 정보를 가져온다
        reply.setArticle(articleService.selectArticleId(articleId));

        // 3. 권한체크
        // 3-1. 관리자면 모든 글에 댓글 달 수 있음
        if(user.getRole().getRoleCode().equals("role_admin")){
            return replyService.createReply(articleId, reply);
        }else if(user.getRole().getRoleCode().equals("role_teacher") || user.getRole().getRoleCode().equals("role_student")){
            // 3-2. 강사 or 학생 인 경우 notice, job, project 의 게시물에는 댓글을 작성 가능
            if(reply.getArticle().getBoardTypeList().getBoardId().equals("notice") ||
                    reply.getArticle().getBoardTypeList().getBoardId().equals("job") ||
                    reply.getArticle().getBoardTypeList().getBoardId().equals("project")){
                return replyService.createReply(articleId, reply);
            }else{ // 3-3. 반별 게시물이라면 유저의 반정보를 비교하여 일치시에만 작성 가능.
                // 현재 댓글을 작성하려는 유저의 반 정보를 가져옴
                Long currentUserClassId = userIdToClassIdConverter.userIdToClassId(user.getUserId());
                // 해당 반의 게시글이라면 댓글 작성 가능
                if(currentUserClassId.equals(reply.getArticle().getBoardTypeList().getClassGroup().getClassId())){
                    return replyService.createReply(articleId, reply);
                }else{
                    return null;
                }
            }
        }
        return null;
    }

    // 댓글 전체조회
    // EndPoint :http://localhost:8080/reply/list?boardId=class_1_board&articleId=23
    @PreAuthorize("hasAnyAuthority('NOTICE_READ','JOB_READ','PRO_READ','CBOARD_READ','CNOTICE_READ','LIBRARY_READ')")
    @RequestMapping(
            path = "/list",
            method = RequestMethod.GET,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public ResultItems<Reply> listOf(
            @RequestParam(name = "articleId", required = true) Long articleId,
            @RequestParam(name = "page", defaultValue = "1", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
//        Pageable pageable = PageRequest.of(page - 1, size);
        Pageable pageable = PageRequest.of((page < 1? 0 : page-1),(size<0?10:size), Sort.by("replyId").descending());

        Page<Reply> replyList = replyService.listOfReplyByArticleId(articleId, pageable);

        return new ResultItems<Reply>(replyList.stream().collect(Collectors.toList()), page, size, replyList.getTotalElements());
    }


    // 댓글 수정
    // EndPoint :  http://localhost:8080/reply/view?replyId=5
    @PreAuthorize("hasAnyAuthority('NOTICE_WRITE','JOB_WRITE','PRO_WRITE','CBOARD_WRITE','CNOTICE_WRITE','LIBRARY_WRITE')")
    @RequestMapping(
            path = "/view",
            method = RequestMethod.PATCH,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE })
    public int update(
            Principal principal,
            @RequestParam(name = "replyId", required = true) Long replyId,
            @RequestBody Reply reply) {

        // 1. 댓글을 수정하려는 유저의 ID 와 작성된 댓글의 유저ID 를 비교하기위해 replyOwner 라는 변수에 댓글의 user_id 를 저장해준다.
        Long replyOwner = replyService.findReplyOwnerId(replyId);

        // 2. 현재 댓글을 수정하려는 User의 정보를 저장해준다.
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        reply.setUser(user);

        // 3. replyOwner 와 reply 객체 안에 있는 user_id를 비교해준다.
        if(replyOwner == reply.getUser().getUserId()){
            // 같다면 수정을 한다.
            System.out.println("수정에 성공했습니다.");
            return replyService.updateReply(reply.getReplyContents(), replyId);
        }else{
            // 다르다면 수정을 안한다.
            System.out.println("수정에 실패했습니다.");
            return 0;
        }
    }


    // 댓글 삭제
    // EndPoint :  http://localhost:8080/reply/view?replyId=5
    // Todo : 관리자는 모든 게시물을 삭제할 수 있는 권한을 주어야 함.
    @PreAuthorize("hasAnyAuthority('NOTICE_WRITE','JOB_WRITE','PRO_WRITE','CBOARD_WRITE','CNOTICE_WRITE','LIBRARY_WRITE')")
    @RequestMapping(
            path = "/view",
            method = RequestMethod.DELETE,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE })
    public Reply delete(
            Principal principal,
            Reply reply,
            @RequestParam(name = "replyId", required = true) Long replyId) {

        // 1. 댓글을 수정하려는 유저의 ID 와 작성된 댓글의 유저ID 를 비교하기위해 replyOwner 라는 변수에 댓글의 user_id 를 저장해준다.
        Long replyOwner = replyService.findReplyOwnerId(replyId);

        // 2. 현재 댓글을 수정하려는 User의 정보를 저장해준다.
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        reply.setUser(user);

        // 3. 비교해서 같다면 삭제
        if(replyOwner == reply.getUser().getUserId()){
            System.out.println("삭제에 성공했습니다.");
            replyService.deleteReply(replyId);
        }

        // 이건 잘 모르겠음 ㅈㅅ
        Reply deleteReply = new Reply();
        deleteReply.setReplyId(replyId);
        return deleteReply;
    }

    // 역할 : 해당 게시판의 게시물 하나를 조회
    // 설명 : 우선 boardId 로 게시판을 구별해주고 그 게시판에 해당 게시물이 있으면 조회가 되게 합니다.
    //        boardId 를 비교하여 조회하기 때문에 boardId 와 aritlceId 가 DB에 저장되있는 것과 일치해야됨
    // EndPoint : http://localhost:8080/reply/view?boardId=class_1_board&articleId=28&replyId=1
    @PreAuthorize("hasAnyAuthority('NOTICE_READ','JOB_READ','PRO_READ','CBOARD_READ','CNOTICE_READ','LIBRARY_READ')")
    @RequestMapping(
            path = "/view",
            method = RequestMethod.GET,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public Reply retrieve(
            Principal principal,
            @RequestParam(name = "boardId", required = true) String boardId,
            @RequestParam(name = "articleId", required = true) Long articleId,
            @RequestParam(name = "replyId", required = true) Long replyId) {

        // 1. 반별 게시판들을 해당 반이 아닌 사람들은 보지 못하도록 하기위해 유저 정보를 받아옴
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        // 2. 해당 유저의 반 고유번호를 얻어옴
        Long currentUserClassId = userIdToClassIdConverter.userIdToClassId(user.getUserId());
//
//        // 3. 권한을 비교하여 관리자면 모든 게시물 상세조회 가능, 학생, 강사라면 다른반별 게시물들은 열람 불가능
//        if (user.getRole().getRoleCode().equals("role_admin")) {  // 3-1 관리자는 모든 게시물 상세조회 가능
//            return articleService.itemOfArticleAndBoardId(articleId, boardId).get();
//        } else if (user.getRole().getRoleCode().equals("role_teacher") || user.getRole().getRoleCode().equals("role_student")) { // 3-2 강사와 학생이라면
//            if (boardId.equals("notice") || boardId.equals("job") || boardId.equals("project")) {
//                // notice, job, project 는 모든 반 열람 가능
//                return articleService.itemOfArticleAndBoardId(articleId, boardId).get();
//            } else {
//                // 3-3. 반별 게시물들은 유저의 반 정보와 게시글의 반 정보를 비교해서 일치 시 열람 가능
//                if (currentUserClassId.equals(boardTypeListService.selectClassId(boardId))) {
//                    return articleService.itemOfArticleAndBoardId(articleId, boardId).get();
//                } else {
//                    return null;
//                }
//            }
//        }
        return replyService.itemOfArticleAndBoardIdAndReplyId(articleId,replyId).get();
    }
}
