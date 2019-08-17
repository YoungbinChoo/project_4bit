package com.bitcamp.project.project_4bit.service;

import com.bitcamp.project.project_4bit.entity.StudentTest;
import com.bitcamp.project.project_4bit.repository.StudentTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentTestService {

    @Autowired
    private StudentTestRepository studentTestRepository;

    // 역할 : StudentTest 생성
    @Transactional
    public StudentTest createStudentTest(StudentTest studentTest){
        return studentTestRepository.save(studentTest);
    }

    // 역할 : studentTestRepository에서 반환된 시험 및 학생별 시험점수 넘겨줌
    @Transactional
    public int scoreOfStudent(Long testId, Long userId){
        return studentTestRepository.findScoreByTestIdAndUserId(testId, userId);
    }

    // 역할 : studentTestRepository에서 수정 성공 여부를 반환한다(성공 : 1, 실패 : 0)
    @Transactional
    public int updateStudentTest(int studentScore, Long testId, Long userId){
        return studentTestRepository.updateStudentTest(studentScore, testId, userId);
    }
}