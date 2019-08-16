package com.bitcamp.project.project_4bit.service;

import com.bitcamp.project.project_4bit.entity.BoardTypeList;
import com.bitcamp.project.project_4bit.entity.Branch;
import com.bitcamp.project.project_4bit.entity.ClassGroup;
import com.bitcamp.project.project_4bit.entity.ConstraintDefine;
import com.bitcamp.project.project_4bit.repository.BoardTypeListRepository;
import com.bitcamp.project.project_4bit.repository.BranchRepository;
import com.bitcamp.project.project_4bit.repository.ClassGroupRepository;
import com.bitcamp.project.project_4bit.repository.ConstraintDefineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
/*
작성자     : 이중호, 황서영
작성일시    : 19.08.14 16:00


* */

// admin 이 반을 등록하는 Service
@Service
public class ClassGroupService {

    @Autowired
    private ClassGroupRepository classGroupRepository;

    @Autowired
    private BoardTypeListRepository boardTypeListRepository;

    @Autowired
    private ConstraintDefineRepository constraintDefineRepository;

    @Autowired
    private BranchRepository branchRepository;


    // classId가 오면 classGroup덩어리를 반환해주는 서비스
    @Transactional(readOnly = true)
    public ClassGroup laodClassGroupByClassId(long classId) {
        ClassGroup classGroup = classGroupRepository.findByClassId(classId);
        return classGroup;
    }


    // Todo : 중복체크~
    // 반을 등록
    @Transactional
    public ClassGroup registerClassGroup(ClassGroup classGroup){

        ClassGroup newClassGroup = new ClassGroup();

        // 1. 등록할 새로운 반의 classGroup 의 데이터를 세팅해줌
        newClassGroup.setClassName(classGroup.getClassName());
        newClassGroup.setClassStartDate(classGroup.getClassStartDate());
        newClassGroup.setClassEndDate(classGroup.getClassEndDate());
        newClassGroup.setSubject(classGroup.getSubject());
        newClassGroup.setBranch(classGroup.getBranch());

        // 2. ClassGroup 테이블에 저장
        newClassGroup = classGroupRepository.save(newClassGroup);

        // 3-1. BoardTypeList 테이블에 반별 자유게시판 등록
        BoardTypeList newClassBoard = new BoardTypeList();
        newClassBoard.setBoardId("class_" + newClassGroup.getClassId() + "_board");
        newClassBoard.setClassGroup(newClassGroup);
        newClassBoard.setBoardName(classGroup.getClassName() + " 자유게시판");
        newClassBoard.setConstraintDefine(constraintDefineRepository.findByConstraintName("class_board_constraint"));
        newClassBoard.setArticleLastNumber(0);

        boardTypeListRepository.save(newClassBoard);


        // 3-2. BoardTypeList 테이블에 반별 공지게시판 등록
        BoardTypeList newClassNotice = new BoardTypeList();
        newClassNotice.setBoardId("class_" + newClassGroup.getClassId() + "_notice");
        newClassNotice.setClassGroup(newClassGroup);
        newClassNotice.setBoardName(classGroup.getClassName() + " 공지게시판");
        newClassNotice.setConstraintDefine(constraintDefineRepository.findByConstraintName("class_notice_constraint"));
        newClassNotice.setArticleLastNumber(0);
        boardTypeListRepository.save(newClassNotice);

        // 3-3. BoardTypeList 테이블에 반별 자료게시판 등록
        BoardTypeList newClassLibrary = new BoardTypeList();
        newClassLibrary.setBoardId("class_" + newClassGroup.getClassId() + "_library");
        newClassLibrary.setClassGroup(newClassGroup);
        newClassLibrary.setBoardName(classGroup.getClassName() + " 자료게시판");
        newClassLibrary.setConstraintDefine(constraintDefineRepository.findByConstraintName("class_library_constraint"));
        newClassLibrary.setArticleLastNumber(0);
        boardTypeListRepository.save(newClassLibrary);

        return newClassGroup;
    }


}
