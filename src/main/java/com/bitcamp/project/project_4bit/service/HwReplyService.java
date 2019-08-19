package com.bitcamp.project.project_4bit.service;

import com.bitcamp.project.project_4bit.entity.HwReply;
import com.bitcamp.project.project_4bit.repository.HwReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class HwReplyService {

    @Autowired
    private HwReplyRepository hwReplyRepository;

    // hwReply 생성 메서드
    @Transactional
    public HwReply createHwReply(HwReply hwReply) {
        return hwReplyRepository.save(hwReply);
    }

    // hwArticleId를 넘겨받아 hwReply 중 해당 hwArticle에 달린 댓글을 전부 반환하는 메서드
    @Transactional
    public Page<HwReply> listOfHwReplyByHwArticleId(Long hwArticleId, Pageable pageable) {
        return hwReplyRepository.findAllByHwArticleId(hwArticleId, pageable);
    }

    // hwReplyId를 받아서 해당 hwReply의 원작성자의 userId를 반환하는 메서드
    @Transactional(readOnly = true)
    public Long loadUserIdByHwReplyId(Long hwReplyId) {
        return hwReplyRepository.findByHwReplyId(hwReplyId).getUser().getUserId();
    }

    // hwReply 삭제 메서드
    @Transactional
    public void deleteHwReply(Long hwReplyId) {
        hwReplyRepository.deleteById(hwReplyId);
    }

    // hwReplyId를 받아서 해당하는 HwReply 1개만 반환하는 메서드
    @Transactional(readOnly = true)
    public Optional<HwReply> itemOfHwReply(Long hwReplyId) {
        return hwReplyRepository.findById(hwReplyId);
    }
}
