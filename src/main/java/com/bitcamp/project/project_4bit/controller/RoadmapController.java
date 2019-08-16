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

    // 역할   : 해당 게시판의 로드맵들 전체 출력
    // 설명   : 과목명, 챕터명 에 대한 읽기 권한이 필요하다.
    //         ResultItems 에 관련한 클래스는 model 안에 있습니다.
    //         페이지네이션을 해주기 위해서 Pageable 을 해주는 것이고, URL 에서 RoadmapStageNo 로 스테이지들을 구분 해주어야 하기 때문에
    //         RoadmapStageNo 의 required 만 true 로 해주어 URL 에 필수적으로 작성하도록 합니다.
    @PreAuthorize("hasAnyAuthority('READ_ROADMAP')")
    @RequestMapping(
            path = "/list",
            method = RequestMethod.GET,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public ResultItems<Roadmap> listOf(
//            @RequestParam(name = "roadmapStageNo", required = true) Integer roadmapStageNo,
            @RequestParam(name = "page", defaultValue = "1", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Roadmap> roadmapList = roadmapService.listOfRoadmapStageNo(pageable);

        return new ResultItems<Roadmap>(roadmapList.stream().collect(Collectors.toList()), page, size, roadmapList.getTotalElements());
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
    public Integer retrieve(
            @RequestParam(name = "roadmapStageNo", required = true) Integer roadmapStageNo) {

        return roadmapService.itemOfRoadmapAndRoadmapStageNo(roadmapStageNo);
    }

}
