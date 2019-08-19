package com.bitcamp.project.project_4bit.controller;

import com.bitcamp.project.project_4bit.entity.Roadmap;
import com.bitcamp.project.project_4bit.model.ResultItems;
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
@RequestMapping("/roadmap")
public class RoadmapController {

    @Autowired
    private RoadmapService roadmapService;

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
            @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
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
    public Integer retrieve(
            @RequestParam(name = "roadmapStageNo", required = true) Integer roadmapStageNo) {

        return roadmapService.itemOfRoadmapAndRoadmapStageNo(roadmapStageNo);
    }

}
