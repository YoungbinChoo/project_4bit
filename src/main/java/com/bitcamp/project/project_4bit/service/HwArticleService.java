package com.bitcamp.project.project_4bit.service;

import com.bitcamp.project.project_4bit.entity.HwArticle;
import com.bitcamp.project.project_4bit.repository.HwArticleRepository;
import com.bitcamp.project.project_4bit.util.UserIdToClassIdConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class HwArticleService {

    @Autowired
    private HwArticleRepository hwArticleRepository;

    @Autowired
    private UserIdToClassIdConverter userIdToClassIdConverter;

    @Transactional
    public HwArticle createHwArticle(HwArticle hwArticle) {
        return hwArticleRepository.save(hwArticle);
    }

    @Transactional
    public Page<HwArticle> listOfHwArticle(Pageable pageable) {
        return hwArticleRepository.findAll(pageable);
    }

    @Transactional
    public Optional<HwArticle> itemOfHwArticle(Long hwArticleId) {
        return hwArticleRepository.findById(hwArticleId);
    }

    @Transactional
    public int updateHwArticle(Long hwArticleId, HwArticle hwArticle) {

        // 사용자가 갱신한 부분(과제 내용)을 hwArticle 덩어리에서 추출해서 String형으로 repository에 전달
        // 갱신 성공시 1, 실패시 0으로 리턴
        String newHwContents = hwArticle.getHwContents();
        return hwArticleRepository.updateHwArticle(hwArticleId, newHwContents);
    }

    @Transactional
    public void deleteHwArticle(Long id) {
        hwArticleRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Long loadUserIdByHwArticleId(Long hwArticleId){
        return hwArticleRepository.findByHwArticleId(hwArticleId).getUser().getUserId();
    }

    @Transactional(readOnly = true)
    public Long loadClassIdByHwArticleId(Long hwArticleId) {
        HwArticle hwArticle = hwArticleRepository.findByHwArticleId(hwArticleId);
        Long userId = hwArticle.getUser().getUserId();
        return userIdToClassIdConverter.userIdToClassId(userId);
    }

    public HwArticle loadHwArticleByHwArticleId(Long hwArticleId) {
        return hwArticleRepository.findByHwArticleId(hwArticleId);
    }
}