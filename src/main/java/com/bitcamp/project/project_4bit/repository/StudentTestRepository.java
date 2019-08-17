package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.StudentTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentTestRepository extends JpaRepository<StudentTest,Long> {

    StudentTest findByStudentTestId(Long studentTestId);

    // 역할 : 완료된 시험 중 학생이 해당 시험을 클릭했을때 시험 점수를 보여준다
    //       testId와 userId를 통해 st_test_score를 검색한다 >> 시험점수를 얻은 후 넘김
    @Query(value = "SELECT st_test_score FROM student_test WHERE test_id = ?1 AND user_id=?2", nativeQuery = true)
    int findScoreByTestIdAndUserId(Long testId, Long userId);

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 역할 : 학생 점수 수정
    //       시험을 보면 점수가 0에서 얻은 점수로 변경
    @Modifying
    @Query(value = "UPDATE student_test SET st_test_score=?1 WHERE test_id =?2 AND user_id = ?3", nativeQuery = true)
    int updateStudentTest(int stTestScore, Long testId, Long userId);

}