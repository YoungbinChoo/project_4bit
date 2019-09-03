package com.bitcamp.project.project_4bit.controller;

import com.bitcamp.project.project_4bit.entity.User;
import com.bitcamp.project.project_4bit.service.LocalUserDetailsService;
import com.bitcamp.project.project_4bit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


/*개인 회원 정보 관리 컨트롤러*/
@RestController
@RequestMapping("/mypage/myinfo")
public class MyInfoController {

    @Autowired
    private LocalUserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    // 회원 개인 정보 읽어오는 컨트롤러
    //endpoint : http://localhost:8080/mypage/myinfo?userId={userId}
    @PreAuthorize("hasAnyAuthority('ME_READ')")
    @RequestMapping(
            method = RequestMethod.GET,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            })
    public User retrieve(Principal principal, @RequestParam("userId") Long userId){

        User user = (User) userDetailsService.loadUserByUsername(principal.getName());

        if(user.getUserId() == userId) {
            return userService.itemOfUser(userId).get();
        }else {
            return null; // todo: 내정보가 아니라면 exception 처리 해주어야 함
        }
    }

    // 회원 개인 정보 수정하는 컨트롤러
    //endpoint : http://localhost:8080/mypage/myinfo/edit?userId={userId}
    @PreAuthorize("hasAnyAuthority('ME_WRITE')")
    @RequestMapping(
            path = "/edit",
            method = RequestMethod.PATCH,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            })
    public User update(Principal principal,
                      @RequestParam(name = "userId", required = true) Long userId,
                      @RequestBody User user
                      ) {

        //본인과 수정하는 유저를 비교해주기 위해 userMe 생성
        User userMe = (User) userDetailsService.loadUserByUsername(principal.getName());

        if (userMe.getUserId() == userId) {

            String userUpdate = userService.updateUserBySelf(userId, user);

            User newUser = userService.updateNewUser(userId);
            return newUser; // update 된 유저를 프론트로 전달하기 위해 변경
        }
        return null; //todo: 본인이 아니면 수정 exception 날려줘야 함.
    }
}
