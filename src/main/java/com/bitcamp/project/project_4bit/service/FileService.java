package com.bitcamp.project.project_4bit.service;

import com.bitcamp.project.project_4bit.entity.File;
import com.bitcamp.project.project_4bit.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;


    // 역할 : 게시물의 파일첨부 글쓰기
    @Transactional
    public File createFile(File file){
        return fileRepository.save(file);
    }
}
