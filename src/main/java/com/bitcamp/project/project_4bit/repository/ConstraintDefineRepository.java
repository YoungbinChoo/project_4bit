package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.ConstraintDefine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConstraintDefineRepository extends JpaRepository<ConstraintDefine, String> {
    ConstraintDefine findByConstraintName(String constraintName);
}

