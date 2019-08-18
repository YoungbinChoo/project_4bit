package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.TestQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TestQuizRepository extends JpaRepository<TestQuiz, Long> {

//    시험 문제를 id로 찾아서 시험을 만들거나, 나중에 학생들 답이랑 비교할 때 문제하나하나 비교하는 메서드
    @Query(value = "SELECT * FROM test_quiz WHERE test_quiz_id=?1", nativeQuery = true)
    TestQuiz findByTestQuizId(Long testQuizId);


}
