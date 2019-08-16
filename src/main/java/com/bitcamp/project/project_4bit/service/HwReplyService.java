package com.bitcamp.project.project_4bit.service;

import com.bitcamp.project.project_4bit.entity.HwReply;
import com.bitcamp.project.project_4bit.repository.HwReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HwReplyService {

    @Autowired
    private HwReplyRepository hwReplyRepository;


    public HwReply createHwReply(HwReply hwReply) {
        return hwReplyRepository.save(hwReply);
    }
}
