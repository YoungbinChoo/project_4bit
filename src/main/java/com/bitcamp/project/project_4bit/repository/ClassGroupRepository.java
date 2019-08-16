package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.ClassGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassGroupRepository extends JpaRepository<ClassGroup,Long> {
    ClassGroup findByClassId(Long classId);

}
