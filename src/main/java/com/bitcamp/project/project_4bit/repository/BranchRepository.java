package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchRepository extends JpaRepository<Branch,String> {
    Branch findByBranchCode(String branchCode);


}
