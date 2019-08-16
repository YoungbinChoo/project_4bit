package com.bitcamp.project.project_4bit.service;

import com.bitcamp.project.project_4bit.entity.StudentTest;
import com.bitcamp.project.project_4bit.repository.StudentTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentTestService {

    @Autowired
    private StudentTestRepository studentTestRepository;

    @Transactional
    public StudentTest createStudentTest(StudentTest studentTest){
        return studentTestRepository.save(studentTest);
    }

}
