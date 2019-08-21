package com.bitcamp.project.project_4bit.service;

import com.bitcamp.project.project_4bit.entity.Quiz;
import com.bitcamp.project.project_4bit.entity.TestQuiz;
import com.bitcamp.project.project_4bit.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
 * 작성일 : 2019.08.13
 * 수정일 : 2019.08.21
 * 메서드 순서
 * 1. createQuiz(Quiz quiz) : 퀴즈 생성
 * 2. findAllByQuiz(Pageable pageable) : 전체퀴즈문제 불러오는 메서드
 * 3. findOneByQuiz(Long quizId) : 1개의 퀴즈 문제 불러오는 메서드
 * 4. findQuizByQuizSubjectAndQuizChapterAndQuizLevel(Pageable pageable, String quizSubject, String quizChapter, String quizLevel)
 *   : 퀴즈 복합 검색(과목, 챕터, 난이도)하는 메서드
 * 5. updateQuiz(String quizContents, String quizAnswer, int quizEachScore, String quizSubject, String quizChapter, String quizLevel, Long quizId)
 *   : 퀴즈 수정
 * 6. readQuizAnswer(Long quizId) : quizId로 quizAnswer 찾기 (학생 답과 문제 답을 비교하기 위해 필요)
 * 7. readQuizEachSCore(Long quizId) : quizId로 quizEachScore 찾기 (학생이 맞은 각 문제 점수 얻기)
 * 8. deleteQuiz(Long quizId) : 문제 삭제
 *  */


@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    //    퀴즈 문제 생성
    @Transactional
    public Quiz createQuiz(Quiz quiz){
        return quizRepository.save(quiz);
    }

    //    퀴즈 전체 목록 불러오기
    @Transactional
    public Page<Quiz> findAllByQuiz(Pageable pageable){
        return quizRepository.findAllByQuiz(pageable);
    }

    //    퀴즈 문제를 1개만 보여주기
    @Transactional
    public Quiz findOneByQuiz(Long quizId){
        return quizRepository.findOneByQuiz(quizId);
    }

    //    역할 : 과목별,챕터별,난이도별로 찾는 메서드
    @Transactional
    public Page<Quiz> findQuizByQuizSubjectAndQuizChapterAndQuizLevel(Pageable pageable, String quizSubject, String quizChapter, String quizLevel){
        return quizRepository.findQuizByQuizSubjectAndQuizChapterAndQuizLevel(pageable, quizSubject, quizChapter, quizLevel);
    }

    //     문제 contents, answer, eachScore, subject, chapter, level 수정
    @Transactional
    public int updateQuiz(String quizContents, String quizAnswer, int quizEachScore, String quizSubject, String quizChapter, String quizLevel, Long quizId){
        return quizRepository.updateQuiz(quizContents, quizAnswer, quizEachScore, quizSubject, quizChapter, quizLevel, quizId);
    }

    // 역할 : quizId로 quizAnswer 찾기
    //      1. 학생 답과 문제 답을 비교하기 위해 필요
    @Transactional
    public String readQuizAnswer(Long quizId){
        return quizRepository.findQuizAnswerByQuizId(quizId);
    }

    // 역할 : quizId로 quizEachScore 찾기
    //      1. 학생이 맞은 각 문제 점수 얻기
    @Transactional
    public int readQuizEachSCore(Long quizId){
        return quizRepository.findquizEachScoreByQuizId(quizId);
    }

    // 역할 : 문제 삭제
    @Transactional
    public void deleteQuiz(Long quizId){
        quizRepository.deleteById(quizId);
    }

}
