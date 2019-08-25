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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 역할 : 문제 번호를 반환
    //      1. 문제 답을 가져오가 위해 문제 번호 필요
    @Transactional
    public Long readQuizId(Long testQuizId){
        return testQuizRepository.findQuizByTestQuizId(testQuizId);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 역할 : testQuizRepository에서 넘긴 문제 개수 반환
    @Transactional
    public int readQuizCount(Long testId){
        return testQuizRepository.findQuizCountByTestId(testId);
    }

    // 역할 : testQuizRepository에서 넘긴 testQuizId를 반환
    //        학생 답과 문제 답을 비교하기 위해
    @Transactional
    public Long readTestQuizId(Long testId, int no){
        return testQuizRepository.findTestQuizIdByTestIdAndTestQuizNo(testId, no);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 역할 :  studentAnswer 수정에 필요한 testQuizId를 반환
    @Transactional
    public Long readTestQuizIdByTestIdAndQuizId(Long testId, Long quizId){
        return testQuizRepository.findTestQuizIdByTestIdAndQuizId(testId, quizId);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 역할: testQuiz 전체 출력 testId로 출력
    @Transactional
    public Page<TestQuiz> findAllOfTestQuizByTestId(Long testId, Pageable pageable){
        return testQuizRepository.findTestQuizByTestQuizId(testId, pageable);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 역할 : 시험 문제 (testId로) 삭제 >> 시험 삭제시 시험 문제도 삭제되어야 함
    @Transactional
    public void deleteTestQuizByTestId(Long testId){
        System.out.println("테스트퀴즈_시험_번호 : " + testId);
        testQuizRepository.deleteByTestId(testId);
    }
}
