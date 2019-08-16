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
 * 메서드 순서
 * 1. createQuiz(Quiz quiz) : 퀴즈 생성
 * 2. findAllByQuiz(Pageable pageable) : 전체퀴즈문제 불러오는 메서드
 * 3. findQuizByQuizSubject(Pageable pageable, String quizSubject) : 과목별 퀴즈문제 불러오는 메서드
 * 4. findQuizByQuizChapter(Pageable pageable, String quizChapter) : 챕터별 퀴즈문제 불러오는 메서드
 * 5. findQuizByQuizLevel(Pageable pageable, String quizLevel) : 난이도별 퀴즈문제 불러오는 메서드
 * 6. updateQuiz(String quizContents, String quizAnswer, int quizEachScore, String quizSubject, String quizChapter, String quizLevel, Long quizId)
 *   : 퀴즈 수정
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

    //    퀴즈 문제를 1개만 보여주기
    @Transactional
    public Page<Quiz> findOneByQuiz(Pageable pageable, Long quizId){
        return quizRepository.findOneByQuiz(pageable, quizId);
    }

    //    퀴즈 전체 목록 불러오기
    @Transactional
    public Page<Quiz> findAllByQuiz(Pageable pageable){
        return quizRepository.findAllByQuiz(pageable);
    }

    //    과목으로 퀴즈 문제 목록
    @Transactional(readOnly = true)
    public Page<Quiz> findQuizByQuizSubject(Pageable pageable, String quizSubject){
        return quizRepository.findQuizByQuizSubject(pageable, quizSubject);
    }

    //    챕터별 퀴즈 문제 목록
    @Transactional(readOnly = true)
    public Page<Quiz> findQuizByQuizChapter(Pageable pageable, String quizChapter){
        return quizRepository.findQuizByQuizChapter(pageable, quizChapter);
    }

    //    난이도별 퀴즈 문제 목록
    @Transactional(readOnly = true)
    public Page<Quiz> findQuizByQuizLevel(Pageable pageable, String quizLevel){
        return quizRepository.findQuizByQuizLevel(pageable, quizLevel);
    }

    //     문제 contents, answer, eachScore, subject, chapter, level 수정
    @Transactional
    public int updateQuiz(String quizContents, String quizAnswer, int quizEachScore, String quizSubject, String quizChapter, String quizLevel, Long quizId){

        return quizRepository.updateQuiz(quizContents, quizAnswer, quizEachScore, quizSubject, quizChapter, quizLevel, quizId);
    }

}
