package com.bitcamp.project.project_4bit.controller;

import com.bitcamp.project.project_4bit.entity.Article;
import com.bitcamp.project.project_4bit.entity.Student;
import com.bitcamp.project.project_4bit.entity.User;
import com.bitcamp.project.project_4bit.model.ResultItems;
import com.bitcamp.project.project_4bit.repository.StudentRepository;
import com.bitcamp.project.project_4bit.service.ArticleService;
import com.bitcamp.project.project_4bit.service.BoardTypeListService;
import com.bitcamp.project.project_4bit.service.LocalUserDetailsService;
import com.bitcamp.project.project_4bit.service.StudentService;
import com.bitcamp.project.project_4bit.util.UserIdToClassIdConverter;
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
작성일시    : 19.08.12
수정일시    : 19.08.19

1. create()
2. listOf()
3. retrieve()
4. update()
5. delete()

* 수정내용
각 CRUD 에 대해서 권한 체크를 해서 해당 권한이 없거나 유저 정보가 불일치 한다면 CRUD 불가
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

    @Autowired
    private UserIdToClassIdConverter userIdToClassIdConverter;

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


        // 4-1. 학생이면 notice, job, project, class_n_notice 에 글을 쓰는 것을 막음
        if (user.getRole().getRoleCode().equals("role_student")) {
            if (boardId.equals("notice") || boardId.equals("job") || boardId.equals("project")
                    || boardId.equals("class_" + article.getBoardTypeList().getClassGroup().getClassId() + "_notice")) {
                return null;
            } else {      // 그 외( class_n_board, class_n_library 엔 글을 씀
                // 현재 글쓴이(학생)의 반 고유번호를 뽑아옴
                Long currentUserClassId = userIdToClassIdConverter.userIdToClassId(user.getUserId());

                // 글쓴이의 반 정보와 글 쓰려는 곳의 반 정보를 비교
                if (currentUserClassId.equals(article.getBoardTypeList().getClassGroup().getClassId())) {
                    return articleService.createArticle(article);       // 일치되면 글 작성
                } else {      // 일치가 안되면 막음
                    return null;
                }
            }
        } else if (user.getRole().getRoleCode().equals("role_teacher")) {      // 4-2. 강사라면 notice, job, project 에 글을 쓰는 것을 막음
            if (boardId.equals("notice") || boardId.equals("job") || boardId.equals("project")) {
                return null;
            } else {      // 그 외는 허가

                // 현재 글쓴이(강사)의 반 번호를 뽑아옴
                Long currentUserClassId = userIdToClassIdConverter.userIdToClassId(user.getUserId());

                // 글쓴이의 반 정보와 글 쓰려는 곳의 반 정보를 비교
                if (currentUserClassId.equals(article.getBoardTypeList().getClassGroup().getClassId())) {     // 일치되면 글 작성
                    return articleService.createArticle(article);
                } else {                  // 일치가 안되면 막음
                    return null;
                }
            }
        } else if (user.getRole().getRoleCode().equals("role_admin")) {        // 4-3. 관리자면 모든 board 에 글을 쓸 수 있음
            return articleService.createArticle(article);
        }
        return null;        // 학생, 강사, 관리자의 권한이 없다면 막음.
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
            Principal principal,
            @RequestParam(name = "boardId", required = true) String boardId,
            @RequestParam(name = "page", defaultValue = "1", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size) {

        // 1. 반별 게시판들을 해당 반이 아닌 사람들은 보지 못하도록 하기위해 유저 정보를 받아옴
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        // 2. 해당 유저의 반 고유번호를 얻어옴
        Long currentUserClassId = userIdToClassIdConverter.userIdToClassId(user.getUserId());

        // 3. 게시글들을 모두 articleList에 저장
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Article> articleList = articleService.listOfArticleByBoardId(boardId, pageable);


        // 4-1 . 유저 권한이 관리자라면 모든 게시판 열람 허용
        if (user.getRole().getRoleCode().equals("role_admin")) {
            return new ResultItems<Article>(articleList.stream().collect(Collectors.toList()), page, size, articleList.getTotalElements());
        } else if (user.getRole().getRoleCode().equals("role_student") || user.getRole().getRoleCode().equals("role_teacher")) { // 4-2. 유저 권한이 학생 또는 강사라면
            // 4-2-(1). notice, job, project 게시판은 모든 반 강사, 학생 열람 가능
            if (boardId.equals("notice") || boardId.equals("job") || boardId.equals("project")) {
                return new ResultItems<Article>(articleList.stream().collect(Collectors.toList()), page, size, articleList.getTotalElements());
            } else {// 4-2-(2). 반별 게시판들 (class_n_notice, class_n_board, class_n_library) 는 해당 반 사람들만 열람 가능
                if (currentUserClassId.equals(boardTypeListService.selectClassId(boardId))) {
                    return new ResultItems<Article>(articleList.stream().collect(Collectors.toList()), page, size, articleList.getTotalElements());
                } else { // 4-2-(3). 해당 반이 아니라면 열람 불가
                    return null;
                }
            }
        }
        return null; // 위의 세 권한 이외면 열람 불가
    }


    // 역할 : 해당 게시판의 게시물 하나를 조회
    // 설명 : 우선 boardId 로 게시판을 구별해주고 그 게시판에 해당 게시물이 있으면 조회가 되게 합니다.
    //        boardId 를 비교하여 조회하기 때문에 boardId 와 aritlceId 가 DB에 저장되있는 것과 일치해야됨
    // EndPoint : http://localhost:8080/board/view?boardId=class_1_board&articleId=28
    //            http://localhost:8080/board/view?boardId=notice&articleId=1
    //            http://localhost:8080/board/view?boardId=job&articleId=3
    //            http://localhost:8080/board/view?boardId=project&articleId=4
    //            http://localhost:8080/board/view?boardId=class_1_board&articleId=7
    //            http://localhost:8080/board/view?boardId=class_1_notice&articleId=8
    //            http://localhost:8080/board/view?boardId=class_1_library&articleId=9
    @PreAuthorize("hasAnyAuthority('NOTICE_READ','JOB_READ','PRO_READ','CBOARD_READ','CNOTICE_READ','LIBRARY_READ')")
    @RequestMapping(
            path = "/view",
            method = RequestMethod.GET,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public Article retrieve(
            Principal principal,
            @RequestParam(name = "boardId", required = true) String boardId,
            @RequestParam(name = "articleId", required = true) Long articleId) {

        // 1. 반별 게시판들을 해당 반이 아닌 사람들은 보지 못하도록 하기위해 유저 정보를 받아옴
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        // 2. 해당 유저의 반 고유번호를 얻어옴
        Long currentUserClassId = userIdToClassIdConverter.userIdToClassId(user.getUserId());

        // 3. 권한을 비교하여 관리자면 모든 게시물 상세조회 가능, 학생, 강사라면 다른반별 게시물들은 열람 불가능
        if (user.getRole().getRoleCode().equals("role_admin")) {  // 3-1 관리자는 모든 게시물 상세조회 가능
            return articleService.itemOfArticleAndBoardId(articleId, boardId).get();
        } else if (user.getRole().getRoleCode().equals("role_teacher") || user.getRole().getRoleCode().equals("role_student")) { // 3-2 강사와 학생이라면
            if (boardId.equals("notice") || boardId.equals("job") || boardId.equals("project")) {
                // notice, job, project 는 모든 반 열람 가능
                return articleService.itemOfArticleAndBoardId(articleId, boardId).get();
            } else {
                // 3-3. 반별 게시물들은 유저의 반 정보와 게시글의 반 정보를 비교해서 일치 시 열람 가능
                if (currentUserClassId.equals(boardTypeListService.selectClassId(boardId))) {
                    return articleService.itemOfArticleAndBoardId(articleId, boardId).get();
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    // 역할 : 해당 게시물을 수정
    // 설명 : 반환형이 int 인 이유는 update 쿼리문을 사용하기 때문에 그렇습니다. 포스트맨에서 테스트를 하면 실행 결과로 1 또는 0 이런식으로 나옵니다.
    //       게시글의 제목, 내용을 수정 할 수 있습니다. (날짜는 now() 를 사용해서 자동으로 수정한 시간으로 세팅 됩니다.)
    // EndPoint :  http://localhost:8080/board/view?articleId=8
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
            @RequestParam(name = "articleId", required = true) Long articleId,
            @RequestBody Article article) {

        // 1. 게시글을 수정하려는 유저의 ID 와 작성된 게시글의 유저 ID를 비교하기 위해 articleOwner 라는 변수에 user_id를 저장
        Long articleOwner = articleService.findArticleOwnerId(articleId);

        // 2. 현재 게시글을 수정하려는 User 정보를 저장해준다.
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        article.setUser(user);

        // 3. 비교해준다. 같으면 수정, 다르면 수정X
        // 3-1. 관리자라면 모든 게시물 수정 가능
        if (user.getRole().getRoleCode().equals("role_admin")) {
            return articleService.updateArticle(article.getArticleTitle(), article.getArticleContents(), articleId);
        } else if (user.getRole().getRoleCode().equals("role_teacher") || user.getRole().getRoleCode().equals("role_student")) {
            // 3-2. 강사와 학생이라면 작성글의 유저 ID 와 현재 수정하려는 유저ID 를 비교하여 같은 경우 수정
            if (articleOwner == article.getUser().getUserId()) {
                return articleService.updateArticle(article.getArticleTitle(), article.getArticleContents(), articleId);
            } else {
                return 0;
            }
        }
        return 0;
    }



    // 역할 : 해당 게시물을 지웁니다.
    // 설명 : articleId를 사용해서 해당 게시물을 DB에서 아예 삭제합니다.
    //        ?boardId=  이쪽은 실제로 사용은 하지 않지만 URL 통일을 위해 무의미하게 써줌. URL에서는 작성해주어야됨.
    // EndPoint :  http://localhost:8080/board/view?articleId=28
    @PreAuthorize("hasAnyAuthority('NOTICE_WRITE','JOB_WRITE','PRO_WRITE','CBOARD_WRITE','CNOTICE_WRITE','LIBRARY_WRITE')")
    @RequestMapping(
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
        // 3-1. 관리자면 모든 게시글 삭제 가능
        if(user.getRole().getRoleCode().equals("role_admin")) {
            // 해당 게시글이 없다면 삭제 불가.
            if(articleService.itemOfArticleId(articleId).isPresent()==false){
                return null;
            }else{  // 해당 게시글이 있다면 삭제
                articleService.deleteArticle(articleId);
                Article deleteArticle = new Article();
                deleteArticle.setArticleId(articleId);
                return deleteArticle;
            }
        }else if(user.getRole().getRoleCode().equals("role_teacher") || user.getRole().getRoleCode().equals("role_student")){
            // 3-2. 강사나 학생이라면 유저 ID를 비교해서 일치하면 삭제
            if(articleOwner == article.getUser().getUserId()){
                // 해당 게시글이 없다면 삭제 불가
                if(articleService.itemOfArticleId(articleId).isPresent()==false){
                    return null;
                }else{
                    // 있다면 삭제
                    articleService.deleteArticle(articleId);
                    Article deleteArticle = new Article();
                    deleteArticle.setArticleId(articleId);
                    return deleteArticle;
                }
            }
        }
        return null;        // 관리자 강사 학생 이외에는 삭제 불가.
    }
}
