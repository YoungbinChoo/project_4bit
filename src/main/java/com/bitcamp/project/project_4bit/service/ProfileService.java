package com.bitcamp.project.project_4bit.service;

import com.bitcamp.project.project_4bit.entity.Admin;
import com.bitcamp.project.project_4bit.entity.ClassTeacherLog;
import com.bitcamp.project.project_4bit.entity.Student;
import com.bitcamp.project.project_4bit.repository.AdminRepository;
import com.bitcamp.project.project_4bit.repository.ClassTeacherLogRepository;
import com.bitcamp.project.project_4bit.repository.StudentRepository;
import com.bitcamp.project.project_4bit.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ProfileService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ClassTeacherLogRepository classTeacherLogRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Transactional
    public Admin selectAdmin(Long userId){
        return adminRepository.findByUser_UserId(userId);
    }


    @Transactional
    public ClassTeacherLog selectTeacher(Long userId){
        // 1. 컨트롤러에서 전달받은 userId 로 teacher 테이블의 user의 userId 로 강사Id를 찾아 저장
        Long teacherId = teacherRepository.findByUser_UserId(userId).getTeacherId();

        // 2. 1에서 찾은 teacherId 를 이용하여 classTacherLog의 강사정보를 찾아 리턴
        return classTeacherLogRepository.findAllByTeacher_TeacherId(teacherId);
    }


    @Transactional
    public Student selectStudent(Long userId){
        return studentRepository.findByUser_UserId(userId);
    }

}
