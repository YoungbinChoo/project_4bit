package com.bitcamp.project.project_4bit.controller;

import com.bitcamp.project.project_4bit.entity.RoadmapExercise;
import com.bitcamp.project.project_4bit.model.ResultItems;
import com.bitcamp.project.project_4bit.service.RoadmapExerciseService;
import com.bitcamp.project.project_4bit.service.RoadmapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/roadmapexercise")
public class RoadmapExerciseController {

    @Autowired
    private RoadmapService roadmapService;

    @Autowired
    private RoadmapExerciseService roadmapExerciseService;

    // 역할   : RoadmapExercise 내용들 전체 출력
    // http://localhost:8080/roadmapexercise/list
    @PreAuthorize("hasAnyAuthority('ROADMAP_READ')")
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
//        Pageable pageable = PageRequest.of(page - 1, size);
        Pageable pageable = PageRequest.of((page < 1? 0 : page-1),(size<0?10:size), Sort.by("id").descending());

        Page<RoadmapExercise> roadmapExerciseList = roadmapExerciseService.listOfExerciseSequence(pageable);

        return new ResultItems<RoadmapExercise>(roadmapExerciseList.stream().collect(Collectors.toList()), page, size, roadmapExerciseList.getTotalElements());
    }

    // 역할 : roadmapStageNo에 해당하는 문제 하나를 조회
    // http://localhost:8080/roadmapexercise/view?roadmapStageNo={roadmapStageNo}
    @PreAuthorize("hasAnyAuthority('ROADMAP_READ')")
    @RequestMapping(
            path = "/view",
            method = RequestMethod.GET,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public RoadmapExercise retrieve(
            @RequestParam(name = "roadmapStageNo", required = true) Integer roadmapStageNo) {

        return roadmapExerciseService.itemOfRoadmapExerciseAndExerciseSequence(roadmapStageNo);
    }
}