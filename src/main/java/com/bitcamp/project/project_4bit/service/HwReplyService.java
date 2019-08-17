package com.bitcamp.project.project_4bit.service;

import com.bitcamp.project.project_4bit.entity.HwReply;
import com.bitcamp.project.project_4bit.repository.HwReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class HwReplyService {

    @Autowired
    private HwReplyRepository hwReplyRepository;


    public HwReply createHwReply(HwReply hwReply) {
        return hwReplyRepository.save(hwReply);
    }

    public Page<HwReply> listOfHwReplyByHwArticleId(Long hwArticleId, Pageable pageable) {
        return hwReplyRepository.findAllByHwArticleId(hwArticleId, pageable);
    }
}
