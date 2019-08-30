package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {

    // 해당 articleId에 대한 모든 reply을 출력
    Page<Reply> findAllByArticle_ArticleId(Long articleId, Pageable pageable);

    // 해당 BoardId, articleId에 해당하는 reply 하나를 조회
    @Query(value = "SELECT * FROM reply r WHERE r.article_id=?1 AND r.reply_id=?2 ", nativeQuery = true)
    Optional<Reply> findByReply_ArticleIdAndReplyId(Long articleId, Long replyId);

//    // reply의 ownerId 찾기
//    Long findUserIdByReply_ReplyId(Long replyId);
    @Query(value = "SELECT r.user_id FROM reply r WHERE r.reply_id=?1", nativeQuery = true)
    Long findReplyOwnerId(Long replyId);

    // reply의 contents 수정
    // update 는 반환형이 1/0 이라서 int 형으로 반환해야되는 것이다.
    @Modifying
    @Query(value = "UPDATE Reply r SET r.reply_contents =?1, r.reply_update_date = now() WHERE r.reply_id =?2", nativeQuery = true)
    int updateReply(String replyContests, Long replyId);

}