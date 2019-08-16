package com.bitcamp.project.project_4bit.controller;

/*
작성자     : 이중호
작성일시    : 19.08.13 09:43

1. create()  댓글 생성

2. listOf    댓글들 전체출력

3. update    댓글 수정

4. delete    댓글 삭제
* */

import com.bitcamp.project.project_4bit.entity.Article;
import com.bitcamp.project.project_4bit.entity.Reply;
import com.bitcamp.project.project_4bit.entity.User;
import com.bitcamp.project.project_4bit.model.ResultItems;
import com.bitcamp.project.project_4bit.service.ArticleService;
import com.bitcamp.project.project_4bit.service.LocalUserDetailsService;
import com.bitcamp.project.project_4bit.service.ReplyService;
import com.bitcamp.project.project_4bit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private UserService userService;
    // 댓글 작성
    // EndPoint : http://localhost:8080/reply/write?boardId=class_1_board&articleId=23
    @PreAuthorize("hasAnyAuthority('WRITE_NOTICE','WRITE_JOB','WRITE_PRO','WRITE_CBOARD','WRITE_CNOTICE','WRITE_LIBRARY')")
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
//            @PathVariable("boardId") String boardId,
            @RequestParam(name = "boardId", required = true) String boardId,
            @RequestParam(name = "articleId", required = true) Long articleId,
            @RequestBody Reply reply) {

        // 1. 유저들의 정보(권한) 을 세팅해준다.
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        reply.setUser(user);

        // 2. 게시글(article)의 정보를 가져온다
        reply.setArticle(articleService.selectArticleId(articleId));


        return replyService.createReply(articleId, reply);
    }

    // 댓글 전체조회
    // EndPoint :http://localhost:8080/reply/list?boardId=class_1_board&articleId=23
    @PreAuthorize("hasAnyAuthority('READ_NOTICE','READ_JOB','READ_PRO','READ_CBOARD','READ_CNOTICE','READ_LIBRARY')")
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
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Reply> replyList = replyService.listOfReplyByArticleId(articleId, pageable);

        return new ResultItems<Reply>(replyList.stream().collect(Collectors.toList()), page, size, replyList.getTotalElements());
    }


    // 댓글 수정
    // EndPoint :  http://localhost:8080/reply/view?replyId=5
    @PreAuthorize("hasAnyAuthority('WRITE_NOTICE','WRITE_JOB','WRITE_PRO','WRITE_CBOARD','WRITE_CNOTICE','WRITE_LIBRARY')")
    @RequestMapping(
            path = "/view",
            method = RequestMethod.PATCH,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
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
            return replyService.updateReply(reply.getReplyContents(), replyId);
        }else{
            // 다르다면 수정을 안한다.
            return 0;
        }
    }


    // 댓글 삭제
    // EndPoint :  http://localhost:8080/reply/view?replyId=5
    @PreAuthorize("hasAnyAuthority('WRITE_NOTICE','WRITE_JOB','WRITE_PRO','WRITE_CBOARD','WRITE_CNOTICE','WRITE_LIBRARY')")
    @RequestMapping(
            path = "/view",
            method = RequestMethod.DELETE,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
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
            replyService.deleteReply(replyId);
        }

        // 이건 잘 모르겠음 ㅈㅅ
        Reply deleteReply = new Reply();
        deleteReply.setReplyId(replyId);
        return deleteReply;
    }
}
