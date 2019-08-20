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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 역할 : 학생이 입력한 답을 반환
    @Transactional
    public String readStudentAnswer(Long studentTestId, Long testQuizId){
        return studentAnswerRepository.findStudentTestAnswerByStudentTestIdAndTestQuiz(studentTestId, testQuizId);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 역할 : testQuizId 반환
    @Transactional
    public Long readQuizTestId(Long studentAnswerId){
        return studentAnswerRepository.findTestQuizIdByStudentAnswerId(studentAnswerId);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 역할 : studentAnswer 수정
    @Transactional
    public int updateStudentAnswer(String studentTestAnswerContent, Long studentTestId, Long testQuizId){
        return studentAnswerRepository.updateStudentAnswer(studentTestAnswerContent, studentTestId, testQuizId);
    }
}
