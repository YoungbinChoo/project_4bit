package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    File findByFileName(String fileName);
}
