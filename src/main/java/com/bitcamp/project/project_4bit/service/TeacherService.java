package com.bitcamp.project.project_4bit.service;

import com.bitcamp.project.project_4bit.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Transactional(readOnly = true)
    public Long loadTeacherIdByUserId(Long userId) {
        return teacherRepository.findOneByUserId(userId);
    }
}
