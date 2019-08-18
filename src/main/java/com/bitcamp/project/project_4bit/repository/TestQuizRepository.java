package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.TestQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestQuizRepository extends JpaRepository<TestQuiz, Long> {

    TestQuiz findByTestQuizId(Long testQuizId);


}
