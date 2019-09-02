package com.bitcamp.project.project_4bit.service;

import com.bitcamp.project.project_4bit.entity.Reply;
import com.bitcamp.project.project_4bit.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ReplyService {

    @Autowired
    private ReplyRepository replyRepository;


    // 역할 : 게시글에 댓글 작성
    @Transactional
    public Reply createReply(Long articleId,Reply reply) {
        return replyRepository.save(reply);
    }


    // 역할 : 게시글의 댓글 전체 출력
    @Transactional(readOnly = true)
    public Page<Reply> listOfReplyByArticleId(Long articleId, Pageable pageable){
        return replyRepository.findAllByArticle_ArticleId(articleId, pageable);
    }

    // 역할 : 댓글 수정
    @Transactional
    public int updateReply(String replyContents, Long replyId){
        return replyRepository.updateReply(replyContents, replyId);
    }

    // 역할 : 댓글의 userId 찾기
    @Transactional
    public Long findReplyOwnerId(Long replyId) {
        return replyRepository.findReplyOwnerId(replyId);
    }

    // 역할    : ReplyId로 게시글 삭제
    // 설명    : ReplyId를 통해서 해당 번호의 게시물을 삭제
    @Transactional
    public void deleteReply(Long replyId) {
        replyRepository.deleteById(replyId);
    }

    // 역할    : BoardId 와 ArticleId를 통해 게시물 상세보기
    // 설명    : 해당 boardId 에 포함되어 있는 article 들을 뽑아낸다.
    //           해당 게시판에 없는 articleId를 사용하면 404error가 발생 -> 나중에 처리해주어야 함
    @Transactional(readOnly = true)
    public Optional<Reply> itemOfArticleAndBoardIdAndReplyId(Long articleId, Long replyId){
        return replyRepository.findByReply_ArticleIdAndReplyId(articleId,replyId);
    }
}
