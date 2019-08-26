package com.bitcamp.project.project_4bit.controller;
import com.bitcamp.project.project_4bit.entity.PointLog;
import com.bitcamp.project.project_4bit.entity.User;
import com.bitcamp.project.project_4bit.model.ResultItems;
import com.bitcamp.project.project_4bit.service.LocalUserDetailsService;
import com.bitcamp.project.project_4bit.service.PointLogService;
import org.hibernate.type.PrimitiveCharacterArrayNClobType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.stream.Collectors;


//작성자 : 황서영
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
    public ResultItems<PointLog> readPointLog(
                        Principal principal,
                        @RequestParam("userId") Long userId,@RequestParam(name = "page", defaultValue = "1", required = false) int page,
                        @RequestParam(name = "size", defaultValue = "15", required = false) int size){

        User user = (User)userDetailsService.loadUserByUsername(principal.getName());

        if (user.getUserId() == userId){
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<PointLog> pointLogs = pointLogService.listofPointLog(pageable);
            return new ResultItems<PointLog>(pointLogs.stream().collect(Collectors.toList()),page,size,pointLogs.getTotalElements());
        }else {
            return null; // todo: 내정보가 아니라면 exception 처리 해주어야 함
        }
    }


    // 과제, 시험에서 바로 포인트 날릴거라서 사실상 필요 없는 메서드
    //포인트 개별로 날리는 메서드
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

        // user의 기존 포인트에 부여된 point가 더해지면서 log로 저장됨
        return pointLogService.addedPointLog(pointLog);
    }
}