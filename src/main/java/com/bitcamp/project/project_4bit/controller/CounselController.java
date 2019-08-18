package com.bitcamp.project.project_4bit.controller;

import com.bitcamp.project.project_4bit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/study/studentstatus")
public class CounselController {

    /*강사 학생현황 화면에서 상담 내역 가져오는 컨트롤러*/

    @Autowired
    private UserService userService;


}
