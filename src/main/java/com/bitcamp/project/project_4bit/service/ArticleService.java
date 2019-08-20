package com.bitcamp.project.project_4bit.service;


import com.bitcamp.project.project_4bit.entity.Article;
import com.bitcamp.project.project_4bit.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/*
작성자     : 이중호
작성일시    : 19.08.12 15:28

1. createArticle()
2. listOfArticleByBoardId()
3. itemOfArticleAndBoardId()
4. updateArticle()
5. deleteArticle()

* */
@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    // 역할    :게시글 쓰기 (controller 에서 사용)
    // 설명    : 실제 DB에 접근해서 .save() 를 통해 저장한다. Article 클래스 안의 내용을 article 객체로 반환하여 DB에 저장한다.
    //           .save() 가 insert 문을 생성해준다.
    @Transactional
    public Article createArticle(Article article) {
        return articleRepository.save(article);
    }

    // 역할    : BoardId 에 따라서 게시물 전체출력
    // 설명    : findAll 은 Page 인터페이스 안에 구현되있는 구현체이다.
    //           findAll 뒤에 By를 붙여 세부 조건을 걸어준다.
    //           ByOOO : OOO 에 테이블을 넣으면 그 테이블을 where 절에 넣어 쿼리를 만들어준다.
    //           ByOOO_ㅁㅁㅁ : OOO 테이블 안의 ㅁㅁㅁ 컬럼을 조건으로 검색을 한다.
    @Transactional(readOnly = true)
    public Page<Article> listOfArticleByBoardId(String boardId, Pageable pageable){
         return articleRepository.findAllByBoardTypeList_BoardId(boardId, pageable);
    }

    // 역할    : BoardId 와 ArticleId를 통해 게시물 상세보기
    // 설명    : 해당 boardId 에 포함되어 있는 article 들을 뽑아낸다.
    //           해당 게시판에 없는 articleId를 사용하면 404error가 발생 -> 나중에 처리해주어야 함
    @Transactional(readOnly = true)
    public Optional<Article> itemOfArticleAndBoardId(Long articleId, String boardId){
        return articleRepository.findByBoardTypeList_BoardIdAndArticleId(boardId,articleId);
    }

    @Transactional
    public Optional<Article> itemOfArticleId(Long articleId){
        return articleRepository.findById(articleId);
    }

    // 역할    : 게시글 제목, 내용을 수정
    // 설명    : Article 의 articleTitle, articleContents 를 수정하여 save 를 통해 다시 저장하는 작업이다.
    //          반환형이 int 인 이유는 articleRepository 에서 커스텀 쿼리를 사용하는데
    //          update 문은 1 또는 0 을 반환하여서 int 형으로 반환해야한다.
    @Transactional
    public int updateArticle(String articleTitle, String articleContents, Long articleId){

        return articleRepository.updateArticle(articleTitle, articleContents, articleId);
    }

    // 역할    : ArticleId로 게시글 삭제
    // 설명    : articleId를 통해서 해당 번호의 게시물을 삭제
    @Transactional
    public void deleteArticle(Long articleId) {
        articleRepository.deleteById(articleId);
    }

    //역할 : ArticleId를 선택
    @Transactional
    public Article selectArticleId(Long articleId){
        return articleRepository.findByArticleId(articleId);
    }

    // 역할 : 작성된 article의 user_id 를 찾아꺼내오는 역할.
    @Transactional
    public Long findArticleOwnerId(Long articleId){
        return articleRepository.findArticleOwner(articleId);
    }

}
