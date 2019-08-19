package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.TestQuiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestQuizRepository extends JpaRepository<TestQuiz, Long> {

//    나중에 학생들 답이랑 비교할 때 문제하나하나 비교하는 메서드
    @Query(value = "SELECT * FROM test_quiz WHERE test_quiz_id=?1", nativeQuery = true)
    TestQuiz findByTestQuizId(Long testQuizId);

//    시험을 전체 보여주는
    @Query(value = "SELECT * FROM test_quiz", nativeQuery =true)
    Page<TestQuiz> findAllByTestQuiz(Pageable pageable);

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 역할 : quizId 반환
    @Query(value = "SELECT quiz_id FROM test_quiz WHERE test_quiz_id = ?1", nativeQuery = true)
    Long findQuizByTestQuizId(Long testQuizId);
}
