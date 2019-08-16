package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
/*
 * 작성일 : 2019.08.13
 * 메서드 순서
 * 1. findAllByQuiz();
 * 2. findQuizByQuizSubject();
 * 3. findQuizByQuizChapter();
 * 4. findQuizByQuizChapter();
 * 5. findQuizByQuizLevel();
 * 6. updateQuiz();
 * */


@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

    //    역할 : 문제 전체 리스트 보여주기
    @Query(value = "SELECT * FROM quiz", nativeQuery = true)
    Page<Quiz> findAllByQuiz(Pageable pageable);

    //    역할 : 문제 1개를 보여주는 메서드
    @Query(value = "SELECT * FROM quiz WHERE quiz_id=?1", nativeQuery = true)
    Page<Quiz> findOneByQuiz(Pageable pageable, Long quizId);

    //    역할 : 과목별,챕터별,난이도별로 찾는 메서드
    @Query(value = "SELECT * FROM quiz WHERE quiz_subject=?1 OR quiz_chapter=?2 OR quiz_level=?3", nativeQuery = true)
    Page<Quiz> findQuizByQuizSubjectAndQuizChapterAndQuizLevel(Pageable pageable, String quizSubject, String quizChapter, String quizLevel);

    //    역할 : 문제 contents, answer, eachScore, subject, chapter, level을 quizId로 찾아서 수정
    @Modifying
    @Query(value = "UPDATE Quiz q SET q.quiz_contents =?1, q.quiz_answer =?2, q.quiz_each_score =?3, q.quiz_subject =?4, q.quiz_chapter =?5, q.quiz_level =?6 WHERE q.quiz_id =?7", nativeQuery = true)
    int updateQuiz(String quizContents, String quizAnswer, int quizEachScore, String quizSubject, String quizChapter, String quizLevel, Long quizId);


//    //    역할 : 문제를 과목별로 찾기
//    @Query(value = "SELECT * FROM quiz WHERE quiz_subject = ?1", nativeQuery = true)
//    Page<Quiz> findQuizByQuizSubject(Pageable pageable, String quizSubject);
//
//    //    역할 : 문제를 챕터별로 찾기
//    @Query(value = "SELECT * FROM quiz WHERE quiz_chapter = ?1", nativeQuery = true)
//    Page<Quiz> findQuizByQuizChapter(Pageable pageable, String quizChapter);
//
//    //    역할 : 문제를 난이별로 찾기
//    @Query(value = "SELECT * FROM quiz WHERE quiz_level = ?1", nativeQuery = true)
//    Page<Quiz> findQuizByQuizLevel(Pageable pageable, String quizLevel);
}
