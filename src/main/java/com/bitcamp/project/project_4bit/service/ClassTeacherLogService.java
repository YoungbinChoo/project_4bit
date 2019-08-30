package com.bitcamp.project.project_4bit.service;

import com.bitcamp.project.project_4bit.entity.ClassTeacherLog;
import com.bitcamp.project.project_4bit.repository.ClassTeacherLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class ClassTeacherLogService {

    @Autowired
    private ClassTeacherLogRepository classTeacherLogRepository;

    @Transactional(readOnly = true)
    public Long loadClassIdByTeacherId(Long teacherId) {
        return classTeacherLogRepository.findClassIdByTeacherId(teacherId);
    }

    // 반을 등록후 담당 강사를 매핑해주는 서비스
    @Transactional
    public ClassTeacherLog createClassTeacherLog(ClassTeacherLog classTeacherLog){
        return classTeacherLogRepository.save(classTeacherLog);
    }

}
