package com.bitcamp.project.project_4bit.service;

import com.bitcamp.project.project_4bit.entity.Admin;
import com.bitcamp.project.project_4bit.entity.ClassTeacherLog;
import com.bitcamp.project.project_4bit.entity.Student;
import com.bitcamp.project.project_4bit.repository.AdminRepository;
import com.bitcamp.project.project_4bit.repository.ClassTeacherLogRepository;
import com.bitcamp.project.project_4bit.repository.StudentRepository;
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

    @Transactional
    public Admin selectAdmin(Long adminId){
        return adminRepository.findByAdminId(adminId);
    }

    @Transactional
    public ClassTeacherLog selectTeacher(Long teacherId){
        return classTeacherLogRepository.findAllByTeacher_TeacherId(teacherId);
    }

    @Transactional
    public Student selectStudent(Long studentId){
        return studentRepository.findByStudentId(studentId);
    }

}
