package com.bitcamp.project.project_4bit.controller;
import com.bitcamp.project.project_4bit.entity.PointLog;
import com.bitcamp.project.project_4bit.entity.User;
import com.bitcamp.project.project_4bit.service.LocalUserDetailsService;
import com.bitcamp.project.project_4bit.service.PointLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@RestController
@RequestMapping("/mypage/point")
public class PointLogController {
    @Autowired
    private PointLogService pointLogService;
    @Autowired
    private LocalUserDetailsService userDetailsService;


    // 포인트로그 리스트를 보여주는 메서드
    // endpoint : http://localhost:8080/mypage/point?userId={userId}
    @RequestMapping(
            method = RequestMethod.GET,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public Page<PointLog> readPointLog(@RequestParam("userId") Long userId,@RequestParam(name = "page", defaultValue = "1", required = false) int page,
                                       @RequestParam(name = "size", defaultValue = "10", required = false) int size){
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<PointLog> pointLogs = pointLogService.listofPointLog(pageable);
        return pointLogs;
    }

    //포인트 더하는 메서드
    // endpoint : http://localhost:8080/mypage/point
    @RequestMapping(
            method = RequestMethod.POST,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public PointLog addPointLog(Principal principal,@RequestBody PointLog pointLog){
        /*접속된 스스로에게 포인트 부여됨*/
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        pointLog.setUser(user);
        return pointLogService.addedPointLog(pointLog);
    }
}