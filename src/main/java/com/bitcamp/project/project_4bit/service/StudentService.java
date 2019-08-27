package com.bitcamp.project.project_4bit.service;

import com.bitcamp.project.project_4bit.entity.Student;
import com.bitcamp.project.project_4bit.entity.User;
import com.bitcamp.project.project_4bit.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


//작성자 : 황서영
@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Transactional(readOnly = true)
    public Student loadStudentByStudentId(Long studentId) {
        return studentRepository.findByStudentId(studentId);
    }

    @Transactional(readOnly = true)
    public Long loadStudentIdByUserId(Long userId) {
        return studentRepository.findOneByUserId(userId);
    }

    @Transactional
    public Student selectStudentByAttendId(String attendId) {
        return studentRepository.findOneByAttendId(attendId);
    }

}
