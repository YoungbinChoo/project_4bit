package com.bitcamp.project.project_4bit.controller;

import com.bitcamp.project.project_4bit.entity.Roadmap;
import com.bitcamp.project.project_4bit.entity.Student;
import com.bitcamp.project.project_4bit.entity.User;
import com.bitcamp.project.project_4bit.exception.AuthException;
import com.bitcamp.project.project_4bit.model.ResultItems;
import com.bitcamp.project.project_4bit.service.LocalUserDetailsService;
import com.bitcamp.project.project_4bit.service.RoadmapService;
import com.bitcamp.project.project_4bit.service.StudentService;
import com.bitcamp.project.project_4bit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/roadmap")
public class RoadmapController {

    @Autowired
    private RoadmapService roadmapService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private LocalUserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    // 역할   : Roadmap 내용들 전체 출력
    // http://localhost:8080/roadmap/list
    @PreAuthorize("hasAnyAuthority('ROADMAP_READ')")
    @RequestMapping(
            path = "/list",
            method = RequestMethod.GET,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public ResultItems<Roadmap> listOf(
            @RequestParam(name = "page", defaultValue = "1", required = false) int page,
            @RequestParam(name = "size", defaultValue = "15", required = false) int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
//        Pageable pageable = PageRequest.of((page < 1? 0 : page-1),(size<0?10:size), Sort.by("id").descending());

        Page<Roadmap> roadmapList = roadmapService.listOfRoadmapStageNo(pageable);

        return new ResultItems<Roadmap>(roadmapList.stream().collect(Collectors.toList()), page, size, roadmapList.getTotalElements());
    }

    // 역할 : Roadmap 내용 하나를 조회
    // http://localhost:8080/roadmap/view?roadmapStageNo={roadmapStageNo}
    @PreAuthorize("hasAnyAuthority('ROADMAP_READ')")
    @RequestMapping(
            path = "/view",
            method = RequestMethod.GET,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public Roadmap retrieve(
            Principal principal,
            @RequestParam(name = "roadmapStageNo", required = true) Integer roadmapStageNo) {

        // 현재 학생의 정보를 가져옴
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        Student currentStudent = new Student();
        currentStudent = studentService.findStudentByUserId(user.getUserId());

        // 로드맵_연습문제 테이블의 스테이지번호와 비교해서 학생의 로드맵마지막 단계보다 작은 문제들만 보여줌
        //todo : 클라이언트로부터 문제를 풀었다는 데이터를 받으면 학생의 로드맵마지막단계를 +1 해서 업데이트
        if (currentStudent.getRoadmapLast() >= roadmapStageNo) {
            return roadmapService.itemOfRoadmapAndRoadmapStageNo(roadmapStageNo);
        } else {
            throw new AuthException("이전 문제를 풀어야됩니다.");
        }
    }

    // 역할 : 문제를 풀면 다음단계로 업데이트
    // http://localhost:8080/roadmap?roadmapLast=1
    @PreAuthorize("hasAnyAuthority('ROADMAP_READ')")
    @RequestMapping(
            method = RequestMethod.PATCH,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE})
    public int updateRoadmapLast(Principal principal,
                                 @RequestParam int roadmapLast) {
        // 현재 유저정보
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        Student currentStudent = studentService.findStudentByUserId(user.getUserId());

        if (roadmapLast == currentStudent.getRoadmapLast()) {
            int lastRoadmap = currentStudent.getRoadmapLast();
            lastRoadmap = lastRoadmap + 1;

            return studentService.updateLastRoadmap(currentStudent.getUser().getUserId(), lastRoadmap);
        } else {
            return 0;
        }
    }



    // 역할: userId를 받아서 해당학생의 마지막 roadmap단계를 돌려주는 메서드
    // http://localhost:8080/roadmap/findlast?userId=1
    @PreAuthorize("hasAnyAuthority('ROADMAP_READ')")
    @RequestMapping(
            path = "/findlast",
            method = RequestMethod.POST,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE})
    public int findRoadmapLast(Principal principal, @RequestParam(name = "userId", required = true) Long userId) {

        int roadmapLast = -1;
        User user = userService.loadUserByUserId(userId);

        // 1. 학생인 경우
        if (user.getRole().getRoleCode().equals("role_student")) {
            Long studentId = studentService.loadStudentIdByUserId(userId);
            Student student = studentService.loadStudentByStudentId(studentId);
            roadmapLast = student.getRoadmapLast();
        }
        return roadmapLast;     // 학생이 아니면 -1
    }
}
