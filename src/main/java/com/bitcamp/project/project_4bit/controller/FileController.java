//package com.bitcamp.project.project_4bit.controller;
//
//import com.bitcamp.project.project_4bit.entity.File;
//import com.bitcamp.project.project_4bit.service.BoardTypeListService;
//import com.bitcamp.project.project_4bit.service.FileService;
//import com.bitcamp.project.project_4bit.service.LocalUserDetailsService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.security.Principal;
//
//@RestController
//@RequestMapping("/file")
//public class FileController {
//
//    @Autowired
//    private LocalUserDetailsService userDetailsService;
//
//    @Autowired
//    private FileService fileService;
//
//    @Autowired
//    private BoardTypeListService boardTypeListService;
//
//
//    // 역할 : 게시물의 첨부파일 첨부(Create)
//    // EndPoint :  http://localhost:8080/file/write
//    @PreAuthorize("hasAnyAuthority('NOTICE_WRITE','JOB_WRITE','PRO_WRITE','CBOARD_WRITE','CNOTICE_WRITE','LIBRARY_WRITE')")
//    @RequestMapping(
//            method = RequestMethod.POST,
//            produces = {
//                    MediaType.APPLICATION_JSON_UTF8_VALUE,
//                    MediaType.APPLICATION_XML_VALUE})
//    public File create(
//            Principal principal,
//            @RequestBody
//    )
//}
