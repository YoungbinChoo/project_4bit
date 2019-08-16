package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.HwArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HwArticleRepository extends JpaRepository<HwArticle, Long> {
    HwArticle findByHwArticleId(Long hwArticleId);

    // HwArticle 수정 (새로운 내용 전달받아서 갱신, 수정일 지금시간으로 갱신. 리턴은 int형으로 성공하면 1 실패하면 0)
    @Modifying
    @Query(value = "UPDATE hw_article h SET h.hw_contents=?2, h.hw_update_date=now() WHERE h.hw_article_id =?1", nativeQuery = true)
    int updateHwArticle(Long hwArticleId, String newHwContents);
}