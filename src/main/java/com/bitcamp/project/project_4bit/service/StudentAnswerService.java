package com.bitcamp.project.project_4bit.service;

import com.bitcamp.project.project_4bit.entity.StudentAnswer;
import com.bitcamp.project.project_4bit.entity.TestGroup;
import com.bitcamp.project.project_4bit.repository.StudentAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentAnswerService {

    @Autowired
    private StudentAnswerRepository studentAnswerRepository;

    // 역할 : 학생 답 생성
    @Transactional
    public StudentAnswer createStudentAnswer(StudentAnswer studentAnswer){
        return studentAnswerRepository.save(studentAnswer);
    }

}
