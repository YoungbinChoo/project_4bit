package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/*
작성자     : 이중호
작성일시    : 19.08.12 15:28

1. findByArticleId()

2. findAllByBoardTypeList_BoardId()

3. findByBoardTypeList_BoardIdAndArticleId()

4. @Query(value = "UPDATE Article a SET a.article_title =?1, a.article_contents =?2, a.article_update_date = now() WHERE a.article_id =?3", nativeQuery = true)
    int updateArticle(String articleTitle, String articleContests, Long articleId)

* */

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    // 해당 Article 테이블에서 where 조건으로 ArticleId를 줘서 찾는것.
    Article findByArticleId(Long articleId);

    // 해당 BoardId에 대한 모든 article을 출력
    Page<Article> findAllByBoardTypeList_BoardId(String boardId, Pageable pageable);

    // 해당 BoardId에 해당하는 article 하나를 조회
    Optional<Article> findByBoardTypeList_BoardIdAndArticleId(String boardId, Long articleId);




    // article의 contents 수정
    // update 는 반환형이 1/0 이라서 int 형으로 반환해야되는 것이다.
    @Modifying
    @Query(value = "UPDATE Article a SET a.article_title =?1, a.article_contents =?2, a.article_update_date = now() WHERE a.article_id =?3", nativeQuery = true)
    int updateArticle(String articleTitle, String articleContests, Long articleId);

    // 작성된 article의 user_id (owner)를 찾는역할
    @Query(value = "SELECT a.user_id FROM article a WHERE a.article_id=?1", nativeQuery = true)
    Long findArticleOwner(Long articleId);

//    // 커스텀 쿼리
//    Page<Todo> findAllByUser(User user, Boolean completed, Pageable pageable);
//
//    By~~~~Order~~~~ 로 도 해결이 안되는 것을 커스텀 쿼리를 통해서 해결.
//    @Modifying
//    @Query(value = "UPDATE Todo t SET t.completed = :completed WHERE t.id = :id")
//    int updateTodoSetCompleted(@Param("id") String id, @Param("completed") Boolean completed);
}
