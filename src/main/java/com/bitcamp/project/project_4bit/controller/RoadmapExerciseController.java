package com.bitcamp.project.project_4bit.controller;

import com.bitcamp.project.project_4bit.entity.RoadmapExercise;
import com.bitcamp.project.project_4bit.model.ResultItems;
import com.bitcamp.project.project_4bit.service.RoadmapExerciseService;
import com.bitcamp.project.project_4bit.service.RoadmapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;


@RestController
@RequestMapping("/roadmapexercise")
public class RoadmapExerciseController {

    @Autowired
    private RoadmapService roadmapService;

    @Autowired
    private RoadmapExerciseService roadmapExerciseService;

    // 역할   : 해당 게시판의 로드맵들 전체 출력
    @PreAuthorize("hasAnyAuthority('READ_ROADMAP')")
    @RequestMapping(
            path = "/list",
            method = RequestMethod.GET,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public ResultItems<RoadmapExercise> listOf(
            @RequestParam(name = "page", defaultValue = "1", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<RoadmapExercise> roadmapExerciseList = roadmapExerciseService.listOfExerciseSequence(pageable);

        return new ResultItems<RoadmapExercise>(roadmapExerciseList.stream().collect(Collectors.toList()), page, size, roadmapExerciseList.getTotalElements());
    }

    // 역할 : 해당 게시판의 게시물 하나를 조회
    @PreAuthorize("hasAnyAuthority('READ_ROADMAP')")
    @RequestMapping(
            path = "/view",
            method = RequestMethod.GET,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public Long retrieve(
           @RequestParam(name = "exerciseSequence", required = true) Long exerciseSequence) {

        return roadmapExerciseService.itemOfRoadmapExerciseAndExerciseSequence(exerciseSequence);
    }
}
