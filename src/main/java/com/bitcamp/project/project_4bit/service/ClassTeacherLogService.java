package com.bitcamp.project.project_4bit.service;

import com.bitcamp.project.project_4bit.entity.ClassGroup;
import com.bitcamp.project.project_4bit.repository.ClassTeacherLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClassTeacherLogService {

    @Autowired
    private ClassTeacherLogRepository classTeacherLogRepository;

    @Transactional(readOnly = true)
    public Long loadClassIdByTeacherId(Long teacherId) {
        return classTeacherLogRepository.findClassIdByTeacherId(teacherId);
    }
}
