package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.ClassTeacherLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassTeacherLogRepository extends JpaRepository<ClassTeacherLog, Long> {

    // class_id 를 통해서 담당 강사 Id 를 찾는 쿼리
    @Query(value = "SELECT log.teacher_id FROM class_teacher_log log WHERE class_id =?1 ORDER BY class_teacher_event_time DESC LIMIT 1;", nativeQuery = true)
    Long findOneByClassGroup_ClassId(Long classId);

    // teacherId를 통해서 classId를 찾는 쿼리
    @Query(value = "SELECT * FROM project_4bit.class_teacher_log l WHERE l.teacher_id=?1 ORDER BY l.class_teacher_event_time DESC LIMIT 1", nativeQuery = true)
    Long findClassIdByTeacherId(Long teacherId);

    // teacherId 를 통해서 해당 teacher 정보 찾는 쿼리
    @Query(value = "SELECT * FROM project_4bit.class_teacher_log l WHERE l.teacher_id=?1 ORDER BY l.class_teacher_event_time DESC LIMIT 1", nativeQuery = true)
    ClassTeacherLog findAllByTeacher_TeacherId(Long teacherId);




}
