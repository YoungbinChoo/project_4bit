package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.StudentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, Long> {

    // 역할 : studentAnswerId으로 학생 입력한 답을 검색
    @Query(value = "SELECT student_test_answer_content FROM student_answer WHERE student_test_id = ?1 AND test_quiz_id = ?2", nativeQuery = true)
    String findStudentTestAnswerByStudentTestIdAndTestQuiz(Long studentTestId, Long testQuizId);

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 역할 :
    @Query(value = "SELECT test_quiz_id FROM student_answer WHERE student_answer_id = ?1", nativeQuery = true)
    Long findTestQuizIdByStudentAnswerId(Long studentAnswerId);

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 역할 : studentAnswer 수정
    @Modifying
    @Query(value = "UPDATE student_answer SET student_test_answer_content = ?1 WHERE student_test_id = ?2 AND test_quiz_id = ?3", nativeQuery = true)
    int updateStudentAnswer(String studentTestAnswerContent, Long studentTestId, Long testQuizId);

}
