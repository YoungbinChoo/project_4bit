package com.bitcamp.project.project_4bit.controller;

import com.bitcamp.project.project_4bit.entity.ClassTeacherLog;
import com.bitcamp.project.project_4bit.service.ClassGroupService;
import com.bitcamp.project.project_4bit.service.ClassTeacherLogService;
import com.bitcamp.project.project_4bit.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manage/classteacher")
public class ClassTeacherLogController {

    @Autowired
    private ClassTeacherLogService classTeacherLogService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private ClassGroupService classGroupService;


    // 반 등록 후 강사를 선택해서 반에 매칭해줌
    @PreAuthorize("hasAnyAuthority('MANAGE_WRITE')")
    @RequestMapping(
            path = "/new/selectteacher",
            method = RequestMethod.POST,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            })
    public ClassTeacherLog registerClassTeacher(@RequestBody ClassTeacherLog classTeacherLog,
                                                @RequestParam Long teacherId,
                                                @RequestParam Long classId){

        classTeacherLog.setClassGroup(classGroupService.laodClassGroupByClassId(classId));
        classTeacherLog.setTeacher(teacherService.selectTeacher(teacherId));

        return classTeacherLogService.createClassTeacherLog(classTeacherLog);
    }
}
