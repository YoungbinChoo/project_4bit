package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.HwReply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HwReplyRepository extends JpaRepository<HwReply, Long> {

    // 전달받은 hwArticleId값을 가진 hwReply들을 찾기(=해당 글에 달린 댓글만 찾기)
    @Query(value = "SELECT * FROM hw_reply WHERE hw_article_id=?1", nativeQuery = true)
    Page<HwReply> findAllByHwArticleId(Long hwArticleId, Pageable pageable);
}
