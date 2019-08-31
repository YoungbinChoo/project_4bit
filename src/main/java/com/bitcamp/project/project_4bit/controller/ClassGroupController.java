package com.bitcamp.project.project_4bit.controller;

import com.bitcamp.project.project_4bit.entity.ClassGroup;
import com.bitcamp.project.project_4bit.entity.ClassTeacherLog;
import com.bitcamp.project.project_4bit.entity.Teacher;
import com.bitcamp.project.project_4bit.entity.User;
import com.bitcamp.project.project_4bit.model.ResultItems;
import com.bitcamp.project.project_4bit.repository.ClassGroupRepository;
import com.bitcamp.project.project_4bit.service.ClassGroupService;
import com.bitcamp.project.project_4bit.service.LocalUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/manage/class")
public class ClassGroupController {

    @Autowired
    private ClassGroupService classGroupService;

    @Autowired
    private LocalUserDetailsService userDetailsService;


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


    // 모든 클래스 정보를 출력
    @PreAuthorize("hasAnyAuthority('MANAGE_READ')")
    @RequestMapping(
            path = "/list",
            method = RequestMethod.GET,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            })
    public ResultItems<ClassTeacherLog> listOfClass(
            Principal principal,
            @RequestParam(name = "page", defaultValue = "1", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size) {

        User user = (User) userDetailsService.loadUserByUsername(principal.getName());

        Pageable pageable = PageRequest.of(page -1, size);
        Page<ClassTeacherLog> classList = classGroupService.listOfClassGroup(pageable);

        if(user.getRole().getRoleCode().equals("role_admin")){
            return new ResultItems<ClassTeacherLog>(classList.stream().collect(Collectors.toList()), page, size, classList.getTotalElements());
        }
        return null;
    }

}
