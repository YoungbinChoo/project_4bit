package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.TestQuiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestQuizRepository extends JpaRepository<TestQuiz, Long> {

//    시험문제를 고유번호로 전체 찾기
    @Query(value = "SELECT * FROM test_quiz WHERE test_quiz_id=?1", nativeQuery = true)
    TestQuiz findByTestQuizId(Long testQuizId);

//    시험문제 리스트를 전체 보여주기
    @Query(value = "SELECT * FROM test_quiz", nativeQuery =true)
    Page<TestQuiz> findAllByTestQuiz(Pageable pageable);

//    시험 문제 수정
    @Modifying
    @Query(value = "UPDATE Test_quiz SET test_quiz_no=?1, test_id=?2, quiz_id=?3 WHERE test_quiz_id =?4", nativeQuery = true)
    int updateTestQuiz(int TestQuizNo, Long TestId, Long QuizId, Long TestQuizId);

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 역할 : quizId 반환
    @Query(value = "SELECT quiz_id FROM test_quiz WHERE test_quiz_id = ?1", nativeQuery = true)
    Long findQuizByTestQuizId(Long testQuizId);
}
