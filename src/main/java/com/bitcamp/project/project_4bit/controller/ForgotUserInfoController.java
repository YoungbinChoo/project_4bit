package com.bitcamp.project.project_4bit.controller;

import com.bitcamp.project.project_4bit.entity.User;
import com.bitcamp.project.project_4bit.repository.UserRepository;
import com.bitcamp.project.project_4bit.service.MailService;
import com.bitcamp.project.project_4bit.util.RandomKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/forgot")
public class ForgotUserInfoController {

    @Autowired
    private MailService mailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RandomKeyGenerator randomKeyGenerator;




    // ID찾기 (등록된 이름과 연락처가 일치하면 화면에 ID 바로 표시)
    // http://localhost:8080/forgot/username?name=학생테스트1&phone=010-6666-6666
    @PostMapping(
            path = "/username",
//            method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Map<String,String> findUsername(@RequestParam(name = "name") String name, @RequestParam(name = "phone") String phone) {
        System.out.println("입력받은 이름: " + name + ", 입력받은 연락처: " + phone);

        // 사용자가 입력한 이름/연락처 정보가 유효한지 확인
        User user = userRepository.findByNameAndPhone(name, phone);

        Map<String,String> result =new HashMap<>();

        if (user == null) {
//            return "해당 이름 또는 연락처로 가입된 회원정보가 존재하지 않습니다.";
            result.put("nothing", "해당 이름 또는 연락처로 가입된 회원정보가 존재하지 않습니다.");
            return result;
        } else {
            String username = user.getUsername();
            result.put(name,"회원님의 ID는" + username + " 입니다");
            return result;
        }
    }



    // 비밀번호 찾기 (등록된 메일로 임시 비밀번호 발송)
    // http://localhost:8080/forgot/password?address=youngbinchoo@gmail.com
    @Transactional
    @RequestMapping(
            path = "/password",
            method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Map<String,String> sendPasswordResetEmail(@RequestParam(name = "address") String address) {

        System.out.println("입력받은 이메일 주소:" + address);
        SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy년 MM월 dd일 HH:mm");
        Date now = new Date();
        String requestedTime = format1.format(now);
        System.out.println(requestedTime);

        Map<String,String> resultMap =new HashMap<>();


        // 사용자가 입력한 이메일 유효한지 확인
        User user = userRepository.findByEmail(address);
        if (user == null) {
            resultMap.put("undefine","해당 이메일로 가입된 회원정보가 존재하지 않습니다.");
            return resultMap;
        } else {

            String userName = user.getName();

            // 임시 비밀번호(10자리 난수) 생성
            String tempPassword = randomKeyGenerator.createRandomKey(10);

            // 생성된 임시 비밀번호로 DB상 회원정보 변경
            Long userId = user.getUserId();
            int result = userRepository.updateUserPassword(userId, tempPassword);

            // 보낼 이메일 제목
            String msgSubject = "4Bit Portal Team: 임시 비밀번호 발급 안내";

            // 보낼 이메일 본문
            String msgText = userName + "님 안녕하세요, 4Bit Portal 입니다.\n" +
                    userName + "님께서 "+ requestedTime +"에 요청하신 임시 비밀번호를 보내드립니다.\n" +
                    "아래 임시 비밀번호로 로그인 하신 후, 마이페이지에서 새로운 비밀번호로 변경하시기 바랍니다. \n\n" +
                    "임시 비밀번호: " + tempPassword + "\n" +
                    "(만일 본인이 비밀번호를 요청한 적 없으면 4bitportal@gmail.com으로 연락주세요)";

            // mailService 통해서 메일 전송 (메일주소, 제목, 본문)
            mailService.sendEmail(address, msgSubject, msgText);

            resultMap.put(userName,"요청하신 이메일 주소 " + address + "로 임시비밀번호를 보내드렸습니다");

            return resultMap;
        }
    }

    @RequestMapping(
            path = "/compare",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Boolean compareUserInfo(@RequestParam("username") String username,
                                   @RequestParam("name") String name,
                                   @RequestParam("phone") String phone) {

        // 사용자가 입력한 ID/이름/연락처 정보가 유효한지 확인
        User user = userRepository.findByUsernameAndNameAndPhone(username,name, phone);

        if (user == null) {
            return false;
        } else {
            String userId = user.getUsername();
            String userName = user.getName();
            String userPhone = user.getPhone();

            if(userId.equals(username) && userName.equals(name) && userPhone.equals(phone)){
                return true;
            }else {
                return false;
            }
        }
    }
}


