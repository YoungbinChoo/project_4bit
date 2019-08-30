package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Teacher findByTeacherId(Long teacherId);

    // UserId 를 사용하여 강사 정보 찾기
    Teacher findByUser_UserId(Long userId);

    @Query(value = "SELECT t.teacher_id FROM teacher t WHERE user_id=?1", nativeQuery = true)
    Long findOneByUserId(Long UserId);


}
