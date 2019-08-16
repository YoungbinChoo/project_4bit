package com.bitcamp.project.project_4bit.controller;

import com.bitcamp.project.project_4bit.entity.ClassGroup;
import com.bitcamp.project.project_4bit.repository.ClassGroupRepository;
import com.bitcamp.project.project_4bit.service.ClassGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/manage/class")
public class ClassGroupController {

    @Autowired
    private ClassGroupService classGroupService;


    // 역할 : admin 이 class 를 등록
    // endpoint : http://localhost:8080/manage/class/new
    //Todo : startDate, EndDate 를 넣는거 하기
    @PreAuthorize("hasAnyAuthority('MANAGE_WRITE')")
    @RequestMapping(
            path = "/new",
            method = RequestMethod.POST,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE})
    public ClassGroup registerMember(@RequestBody ClassGroup classGroup){
        return classGroupService.registerClassGroup(classGroup);
    }
}
