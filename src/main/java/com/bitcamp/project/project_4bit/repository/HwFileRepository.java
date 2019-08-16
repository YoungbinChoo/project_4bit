package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.HwFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HwFileRepository extends JpaRepository<HwFile, Long> {
    HwFile findByHwFileId(long hwFileId);
}