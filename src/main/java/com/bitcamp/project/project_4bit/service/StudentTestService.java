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


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 역할 : studentTestRepository에서 넘긴 총점을 반환
    @Transactional
    public int readSumByTestId(Long testId){
        return studentTestRepository.findSumByTestId(testId);
    }

    // 역할 : studentTestRepository에서 넘긴 시험을 본 학생 수 반환
    @Transactional
    public int readStudentCountByTestId(Long testId){
        return studentTestRepository.findStudentCountByTestId(testId);
    }

    // 역할 : studentTestRepository에서 넘긴 최고값 반환
    @Transactional
    public int readMaxByTestId(Long testId){
        return studentTestRepository.findMaxByTestId(testId);
    }

    // 역할 : studentTestRepository에서 넘긴 최저점 반환
    @Transactional
    public int readMinByTestId(Long testId){
        return studentTestRepository.findMinByTestId(testId);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // 역할 : studentTestRepository에서 넘긴 StudentTest 전체 정보를 반환
    @Transactional
    public StudentTest findStudentTest(Long studentTestId){
        return studentTestRepository.findByStudentTestId(studentTestId);
    }
}