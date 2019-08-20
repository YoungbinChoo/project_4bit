package com.bitcamp.project.project_4bit.service;

import com.bitcamp.project.project_4bit.entity.BoardTypeList;
import com.bitcamp.project.project_4bit.repository.BoardTypeListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/*
작성자     : 이중호
작성일시    : 19.08.12 15:28

1. selectBoardId()


* */
@Service
public class BoardTypeListService {

    @Autowired
    private BoardTypeListRepository boardTypeListRepository;


    // 역할 : ArticleController 에서 boardId를 찾는 것이 필요한데 boardId 를 찾아주는 역할
    @Transactional
    public BoardTypeList selectBoardId(String boardId){
        return boardTypeListRepository.findByBoardId(boardId);
    }

    // 역할 : ArticleController 에서 articleNumber를 1씩 증가시켜야되는데 이것에 대한 마지막 글 번호
    @Transactional
    public int incrementNumber(String boardId){

        // 1. boardTypeListRepository 에서 boardId 를 우선적으로 찾아온다.
        BoardTypeList boardTypeList = boardTypeListRepository.findByBoardId(boardId);

        // 2. newArticleLastNumber 를 boardTypeList의 articleLastNumber를 불러오고 +1 해준다.
        int newArticleLastNumber = boardTypeList.getArticleLastNumber() + 1;

        // 3. 레파지토리에서 updateArticleLastNumber를 실행시켜준다.
        boardTypeListRepository.updateArticleLastNumber(newArticleLastNumber, boardId);

        return newArticleLastNumber;
    }

    @Transactional
    public Long selectClassId(String boardId){
        return boardTypeListRepository.findByBoardId(boardId).getClassGroup().getClassId();
    }
}
