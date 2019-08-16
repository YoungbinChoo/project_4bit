package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.BoardTypeList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/*
작성자     : 이중호
작성일시    : 19.08.12 15:28

1. findByBoardId()

* */

@Repository
public interface BoardTypeListRepository extends JpaRepository<BoardTypeList, String> {

    // BoardId 를 찾는 select 문을 만들어주는 역할
    BoardTypeList findByBoardId(String boardId);

    // BoardTypeList에 있는 artcile_last_number를 update 해준다.
    @Modifying
    @Query(value = "UPDATE board_type_list b SET b.article_last_number =?1 WHERE b.board_id =?2", nativeQuery = true)
    int updateArticleLastNumber(int newArticleLastNumber, String boardId);

}
