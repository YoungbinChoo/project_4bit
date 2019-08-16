package com.bitcamp.project.project_4bit.service;

import com.bitcamp.project.project_4bit.entity.ConstraintDefine;
import com.bitcamp.project.project_4bit.repository.ConstraintDefineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConstraintDefineService {

    @Autowired
    private ConstraintDefineRepository constraintDefineRepository;

    @Transactional(readOnly = true)
    public ConstraintDefine loadConstraintDefineByConstraintName(String constraintName) {
        ConstraintDefine constraintDefine = constraintDefineRepository.findByConstraintName(constraintName);
        return constraintDefine;
    }
}
