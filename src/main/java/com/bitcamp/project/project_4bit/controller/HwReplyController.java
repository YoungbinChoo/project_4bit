package com.bitcamp.project.project_4bit.controller;

import com.bitcamp.project.project_4bit.entity.HwArticle;
import com.bitcamp.project.project_4bit.entity.HwReply;
import com.bitcamp.project.project_4bit.entity.User;
import com.bitcamp.project.project_4bit.service.HwArticleService;
import com.bitcamp.project.project_4bit.service.HwReplyService;
import com.bitcamp.project.project_4bit.service.LocalUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;

@RestController
@RequestMapping("/hwreply")
public class HwReplyController {

    @Autowired
    private HwReplyService hwReplyService;

    @Autowired
    private LocalUserDetailsService userDetailsService;

    @Autowired
    private HwArticleService hwArticleService;




    ///////////////////////////   HwReply 작성(과제댓글, 학생/강사 가능)   ///////////////////////////
    // http://localhost:8080/hwreply/write?hwArticleId={hwArticleId}

    @PreAuthorize("hasAnyAuthority('SHW_WRITE')")
    @RequestMapping(
            path = "/write",
            method = RequestMethod.POST,
            produces = {
                    MediaType.APPLICATION_JSON_UTF8_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public HwReply create(Principal principal,
                          @RequestBody HwReply hwReply,
                          @RequestParam(name = "hwArticleId", defaultValue = "1", required = true) Long hwArticleId) {

        //////////// param으로 넘어오는 HwReply 구성 //////////////////////////////
//        1. HW_reply_id : 사용자입력X, 자동부여
//        2. HW_reply_contents : 사용자입력
//        3. HW_reply_create_date : 사용자입력X, 컨트롤러에서 부여해야
//        4. HW_reply_update_date : 사용자입력X, 컨트롤러에서 부여해야
//        5. HW_article_id : 사용자입력X, 주소줄에서 찾아서 지정
//        6. user_id : 사용자입력X, principal에서 찾아서 지정
        ///////////////////////////////////////////////////////////////////////////////

        // 3. HW_reply_create_date & 4. HW_reply_update_date 세팅
        Date date = new Date();
        hwReply.setHwReplyCreateDate(date);
        hwReply.setHwReplyUpdateDate(date);

        // 5. HW_article_id 세팅
        HwArticle hwArticle = (hwArticleService.loadHwArticleByHwArticleId(hwArticleId));
        hwReply.setHwArticle(hwArticle);

        // 6. user_id 세팅
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        hwReply.setUser(user);

        // 세팅완료된 hwArticle 덩어리를 hwReplyService를 통해 저장(=create)
        return hwReplyService.createHwReply(hwReply);

    }





}
