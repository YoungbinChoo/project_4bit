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


    /*todo : startDate, endDate 입력 해결해야함 : 현재 계속 now()로 들어가고 있음*/

    // 역할 : admin 이 class 를 등록
    // endpoint : http://localhost:8080/manage/class/new
    @PreAuthorize("hasAnyAuthority('MANAGE_WRITE')")
    @RequestMapping(
            path = "/new",
            method = RequestMethod.POST,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            })
    public ClassGroup registerMember(@RequestBody ClassGroup classGroup){
        return classGroupService.registerClassGroup(classGroup);
    }
}
