package com.bitcamp.project.project_4bit.service;

import com.bitcamp.project.project_4bit.entity.TestQuiz;
import com.bitcamp.project.project_4bit.repository.TestQuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestQuizService {

    @Autowired
    private TestQuizRepository testQuizRepository;

    //   역할 : 시험_문제 생성
    @Transactional
    public TestQuiz createTestQuiz(TestQuiz testQuiz){
        return testQuizRepository.save(testQuiz);
    }

    // 역할 : 시험 문제 id로 시험 문제 찾아오는 메서드
    @Transactional
    public TestQuiz findByTestQuizId(Long testQuizId){
        return testQuizRepository.findByTestQuizId(testQuizId);
    }

//    역할 : 시험 문제 전체 찾아오기 메서드
    @Transactional
    public Page<TestQuiz> findAllOfTestQuiz(Pageable pageable){
        return testQuizRepository.findAllByTestQuiz(pageable);
    }

    //   역할 : 시험 문제 수정
    @Transactional
    public int updateTestQuiz(int TestQuizNo, Long TestId, Long QuizId, Long TestQuizId){
        return testQuizRepository.updateTestQuiz(TestQuizNo, TestId, QuizId, TestQuizId);
    }


    // 역할 : 시험 문제 삭제
    @Transactional
    public void deleteTestQuiz(Long testQuizId){
        testQuizRepository.deleteById(testQuizId);
    }
}
