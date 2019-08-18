package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


// 작성자 : 황서영
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByStudentId(Long studentTestId);

    // classId 를 통해서 소속 학생 전체 리스트를 뽑는 Jpa 쿼리
    Page<Student> findAllByClassGroup_ClassId(Long classId, Pageable pageable);

    @Modifying
    @Query(value = "UPDATE student s SET s.student_birth=?2 WHERE s.user_id=?1", nativeQuery = true)
    int updateStudentBySelf(Long userId, String newStudentBirth);

    // 관리자 수정 시 studentBirth, classID 를 수정하는 쿼리.
    @Modifying
    @Query(value = "UPDATE student s SET s.student_birth=?2, s.class_id=?3 WHERE s.user_id=?1", nativeQuery = true)
    int updateStudentByAdmin(Long userId, String newStudentBirth, Long newClassId);

    // userId로 studentId를 찾는 쿼리
    @Query(value = "SELECT s.student_id FROM student s WHERE user_id=?1", nativeQuery = true)
    Long findOneByUserId(Long userId);

    // 강사 학생현황에서 studentId로 counsel 찾아오는 쿼리
    @Query(value = "SELECT s.counsel FROM student s WHERE s.student_id =?1", nativeQuery = true)
    String findCounselByStudentId(Long studentId);

    // 강사 학생현황에서 counsel update 하는 쿼리
    @Modifying
    @Query(value = "UPDATE student s SET s.counsel=?2 WHERE s.student_id=?1", nativeQuery = true)
    int updateCounsel(Long studentId, String counsel);

}