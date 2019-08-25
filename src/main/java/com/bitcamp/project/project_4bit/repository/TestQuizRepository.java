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


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 역할 : 강사가 한 시험에 몇개의 문제를 냈나 체크
    //      >> 학생이 입력한 답과 문제 답을 각각 비교하는 메소드를 for문으로 하기 위해
    @Query(value = "SELECT COUNT(quiz_id) FROM test_quiz WHERE test_id = ?1 GROUP BY test_id", nativeQuery = true)
    int findQuizCountByTestId(Long testId);

    // 역할 : testId와 no(시험지 내 번호)를 통해 testQuizId를 구한다
    @Query(value = "SELECT test_quiz_id FROM test_quiz WHERE test_id = ?1 AND test_quiz_no = ?2", nativeQuery = true)
    Long findTestQuizIdByTestIdAndTestQuizNo(Long testId, int no);


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 역할 :  studentAnswer 수정에 필요한 testQuizId를 반환
    @Query(value = "SELECT test_quiz_id FROM test_quiz WHERE test_id = ?1 AND quiz_id = ?2", nativeQuery = true)
    Long findTestQuizIdByTestIdAndQuizId(Long testId, Long quizId);

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 역할 : testQuiz 전체 출력  testId에 따라
    @Query(value = "SELECT * FROM test_quiz WHERE test_id = ?1", nativeQuery = true)
    Page<TestQuiz> findTestQuizByTestQuizId(Long testId, Pageable pageable);


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 역할 : testQuiz를 testId로 삭제 >> 시험이 삭제되면 시험 문제도 삭제되어야 한다
    @Modifying
    @Query(value = "DELETE FROM test_quiz WHERE test_id = ?1", nativeQuery = true)
    void deleteByTestId(Long testId);
}