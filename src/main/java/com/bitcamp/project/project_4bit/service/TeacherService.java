package com.bitcamp.project.project_4bit.service;

import com.bitcamp.project.project_4bit.entity.Teacher;
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

    // 강사 ID 를 이용하여 해당 강사의 정보를 찾아오는 서비스
    @Transactional
    public Teacher selectTeacher(Long teacherId){
        return teacherRepository.findByTeacherId(teacherId);
    }
}
