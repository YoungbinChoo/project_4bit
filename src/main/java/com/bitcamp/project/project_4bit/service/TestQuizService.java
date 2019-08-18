package com.bitcamp.project.project_4bit.service;

import com.bitcamp.project.project_4bit.entity.TestQuiz;
import com.bitcamp.project.project_4bit.repository.TestQuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestQuizService {

    @Autowired
    private TestQuizRepository testQuizRepository;

    //    시험_문제 생성
    @Transactional
    public TestQuiz createTestQuiz(TestQuiz testQuiz){
        return testQuizRepository.save(testQuiz);
    }


}
