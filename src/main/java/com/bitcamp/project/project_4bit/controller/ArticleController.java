package com.bitcamp.project.project_4bit.controller;

import com.bitcamp.project.project_4bit.entity.Article;
import com.bitcamp.project.project_4bit.entity.User;
import com.bitcamp.project.project_4bit.model.ResultItems;
import com.bitcamp.project.project_4bit.service.ArticleService;
import com.bitcamp.project.project_4bit.service.BoardTypeListService;
import com.bitcamp.project.project_4bit.service.LocalUserDetailsService;
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
작성자     : 이중호
작성일시    : 19.08.12 15:28

1. create()
2. listOf()
3. retrieve()
4. update()
5. delete()

* */


// ================   param 으로 넘어오는 Article 구성 ====================================================
// 1. article_id : 사용자입력 X, Auto_Increment
// 2. article_create_date : 사용자입력 X, 저장한 순간의 시간이 DB에 저장
// 3. article_update_date : 사용자입력 X, 수정한 순간의 시간이 DB에 저장(최초 저장시에는 생성시간과 동일)
// 4. article_hits : 사용자 입력 X, Todo : 누르면 자동으로 증가되도록 로직을 만들어줘야됨
// 5. article_like : 사용자 입력 X, Todo : 누르면 자동으로 증가되도록 로직을 만들어줘야됨
// 6. group_id : 사용자 입력 X,
// 7. depth : 사용자 입력 X,
// 8. sequence : 사용자 입력 X
// 9. article_tile : 사용자 입력.
// 10. article_contents : 사용자 입력.
// 11. user_id : 사용자 입력 X, principal 을 통해서 지정
// 12. board_id : 사용자 입력 X, Client 의 URL 에서 /board 다음의 내용을 받아옴.
// =======================================================================================================



@RestController
@RequestMapping("/board")
public class ArticleController {

    @Autowired
    private LocalUserDetailsService userDetailsService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private BoardTypeListService boardTypeListService;

    // 게시물 작성
    // input : URL 에서의 boardId 를 매개변수 넘겨서 service 로 넘겨서 조건으로 사용할 수 있게 해줍니다.
    // 참고사항: 전체공지, 취업, 프로젝트영상, 반별자유게시물, 반별공지, 반별자료 에 대한 쓰기 권한이 필요하다.
    //          어느 게시판에 게시글을 쓸 건지 정해주어야되므로 setBoardTypeList()를 사용하여 설정해줍니다.
    //          selectBoardId() 는 BoardTypeListService 안에 있습니다.
    // EndPoint :  http://localhost:8080/board/class_1_board/write
    @PreAuthorize("hasAnyAuthority('NOTICE_WRITE','JOB_WRITE','PRO_WRITE','CBOARD_WRITE','CNOTICE_WRITE','LIBRARY_WRITE')")
    @RequestMapping(
            path = "/{boardId}/write",
            method = RequestMethod.POST,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE})
    public Article create(
            Principal principal,
            @PathVariable("boardId") String boardId,
            @RequestBody Article article) {

        // 1. 유저들의 정보(권한) 을 세팅해준다.
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        article.setUser(user);

        // 2. BoardTypeListService에서 boardId를 구해온다.
        article.setBoardTypeList(boardTypeListService.selectBoardId(boardId));

        // 3. BoardTypeList에 있는 article_last_number 를 구해와서 article의 articleNumber에 세팅해준다.   -> 각 게시판마다 게시물 번호는 개별적으로 증가
        article.setArticleNumber(boardTypeListService.incrementNumber(boardId));


        return articleService.createArticle(article);
    }


    // 역할   : 해당 게시판의 게시물들 전체 출력
    // 설명   : 전체공지, 취업, 프로젝트영상, 반별자유게시물, 반별공지, 반별자료 에 대한 읽기 권한이 필요하다.
    //         ResultItems 에 관련한 클래스는 model 안에 있습니다.
    //         페이지네이션을 해주기 위해서 Pageable 을 해주는 것이고, URL 에서 boardId 로 게시판들을 구분 해주어야 하기 때문에
    //         boardId 의 required 만 true 로 해주어 URL 에 필수적으로 작성하도록 합니다.
    // EndPoint : http://localhost:8080/board/list?boardId=class_1_board 로 해주어야 되므로 GET 방식을 사용합니다.
    @PreAuthorize("hasAnyAuthority('NOTICE_READ','JOB_READ','PRO_READ','CBOARD_READ','CNOTICE_READ','LIBRARY_READ')")
    @RequestMapping(
            path = "/list",
            method = RequestMethod.GET,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE})
    public ResultItems<Article> listOf(
            @RequestParam(name = "boardId", required = true) String boardId,
            @RequestParam(name = "page", defaultValue = "1", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size) {


        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Article> articleList = articleService.listOfArticleByBoardId(boardId,pageable);

        return new ResultItems<Article>(articleList.stream().collect(Collectors.toList()), page, size, articleList.getTotalElements());
    }




    // 역할 : 해당 게시판의 게시물 하나를 조회
    // 설명 : 우선 boardId 로 게시판을 구별해주고 그 게시판에 해당 게시물이 있으면 조회가 되게 합니다.
    //        ?boardId=  이쪽은 실제로 사용은 하지 않지만 URL 통일을 위해 무의미하게 써줌. URL에서는 작성해주어야됨.
    // EndPoint : http://localhost:8080/board/view?boardId=class_1_board&articleId=28
    @PreAuthorize("hasAnyAuthority('NOTICE_READ','JOB_READ','PRO_READ','CBOARD_READ','CNOTICE_READ','LIBRARY_READ')")
    @RequestMapping(
//            path = "/{boardId}",
            path = "/view",
            method = RequestMethod.GET,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public Article retrieve(
//            @PathVariable(name = "boardId") String boardId,
            @RequestParam(name = "boardId", required = true) String boardId,
            @RequestParam(name = "articleId", required = true) Long articleId) {

        return articleService.itemOfArticleAndBoardId(articleId,boardId).get();
    }




    // 역할 : 해당 게시물을 수정
    // 설명 : 반환형이 int 인 이유는 update 쿼리문을 사용하기 때문에 그렇습니다. 포스트맨에서 테스트를 하면 실행 결과로 1 또는 0 이런식으로 나옵니다.
    //       게시글의 제목, 내용을 수정 할 수 있습니다. (날짜는 now() 를 사용해서 자동으로 수정한 시간으로 세팅 됩니다.)
    //        ?boardId=  이쪽은 실제로 사용은 하지 않지만 URL 통일을 위해 무의미하게 써줌. URL에서는 작성해주어야됨.
    // EndPoint :  http://localhost:8080/board/view?boardId&articleId=8
    @PreAuthorize("hasAnyAuthority('NOTICE_WRITE','JOB_WRITE','PRO_WRITE','CBOARD_WRITE','CNOTICE_WRITE','LIBRARY_WRITE')")
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
            @RequestParam(name = "boardId", required = true) String boardId,
            @RequestParam(name = "articleId", required = true) Long articleId,
            @RequestBody Article article) {

        // 1. 게시글을 수정하려는 유저의 ID 와 작성된 게시글의 유저 ID를 비교하기 위해 articleOwner 라는 변수에 user_id를 저장
        Long articleOwner = articleService.findArticleOwnerId(articleId);

        // 2. 현재 게시글을 수정하려는 User 정보를 저장해준다.
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        article.setUser(user);

        // 3. 비교해준다. 같으면 수정, 다르면 수정X
        if(articleOwner == article.getUser().getUserId()){
            return articleService.updateArticle(article.getArticleTitle(), article.getArticleContents(), articleId);
        }else{
            return 0;
        }
       }


    // 역할 : 해당 게시물을 지웁니다.
    // 설명 : articleId를 사용해서 해당 게시물을 DB에서 아예 삭제합니다.
    //        ?boardId=  이쪽은 실제로 사용은 하지 않지만 URL 통일을 위해 무의미하게 써줌. URL에서는 작성해주어야됨.
    // EndPoint :  http://localhost:8080/board/view?articleId=28
    @PreAuthorize("hasAnyAuthority('NOTICE_WRITE','JOB_WRITE','PRO_WRITE','CBOARD_WRITE','CNOTICE_WRITE','LIBRARY_WRITE')")
    @RequestMapping(
//            path = "/{articleId}",
            path = "/view",
            method = RequestMethod.DELETE,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public Article delete(
            Principal principal,
            Article article,
            @RequestParam(name = "articleId", required = true) Long articleId) {

        // 1. 게시글을 삭제하려는 유저의 ID와 작성된 게시글의 유저 ID를 비교하기 위해 articleOwner 라는 변수에 게시글의 user_id를 저장
        Long articleOwner = articleService.findArticleOwnerId(articleId);

        // 2. 현재 게시글을 삭제하려는 User 의 정보를 저장
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        article.setUser(user);

        // 3. 같다면 삭제
        if(articleOwner == article.getUser().getUserId()){
            articleService.deleteArticle(articleId);
        }

        // 이건 잘 모르겠음. ㅈㅅ
        Article deleteArticle = new Article();
        deleteArticle.setArticleId(articleId);
        return deleteArticle;
    }
}
