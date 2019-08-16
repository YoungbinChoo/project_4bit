package com.bitcamp.project.project_4bit.util;

import com.bitcamp.project.project_4bit.entity.Student;
import com.bitcamp.project.project_4bit.entity.User;
import com.bitcamp.project.project_4bit.repository.UserRepository;
import com.bitcamp.project.project_4bit.service.ClassTeacherLogService;
import com.bitcamp.project.project_4bit.service.StudentService;
import com.bitcamp.project.project_4bit.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserIdToClassIdConverter {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private ClassTeacherLogService classTeacherLogService;

    public Long userIdToClassId(Long userId) {

        Long classId;
        User user = userRepository.findByUserId(userId);

        // 1. 학생인 경우
        if(user.getRole().getRoleCode().equals("role_student")){
            Long studentId = studentService.loadStudentIdByUserId(userId);
            Student student = studentService.loadStudentByStudentId(studentId);
            classId = student.getClassGroup().getClassId();
            return classId;
        }

        // 2. 강사인 경우
        else if(user.getRole().getRoleCode().equals("role_teacher")) {
            Long teacherId = teacherService.loadTeacherIdByUserId(userId);
            classId = classTeacherLogService.loadClassIdByTeacherId(teacherId);
            return classId;
        }

        // 3. 관리자인 경우
        else {
            return null;
        }
    }

}
