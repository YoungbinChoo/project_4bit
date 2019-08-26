package com.bitcamp.project.project_4bit.controller;

import com.bitcamp.project.project_4bit.entity.User;
import com.bitcamp.project.project_4bit.repository.UserRepository;
import com.bitcamp.project.project_4bit.service.MailService;
import com.bitcamp.project.project_4bit.util.RandomKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/forgot")
public class ForgotUserInfoController {

    @Autowired
    private MailService mailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RandomKeyGenerator randomKeyGenerator;

    @Transactional
    @RequestMapping(
            path = "/password",
            method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public String sendPasswordResetEmail(@RequestParam(name = "address") String address) {
        System.out.println("입력받은 이메일 주소:" + address);

        // 사용자가 입력한 이메일 유효한지 확인
        User user = userRepository.findByEmail(address);
        if (user == null) {
            return "해당 이메일로 가입된 회원정보가 존재하지 않습니다.";
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
            String msgText = "안녕하세요 " + userName + "님, 4Bit Portal 입니다.\n" +
                    userName + "님께서 요청하신 임시 비밀번호를 보내드립니다.\n" +
                    "아래 임시 비밀번호로 로그인 하신 후, 마이페이지에서 새로운 비밀번호로 변경하시기 바랍니다. \n\n" +
                    "임시 비밀번호: " + tempPassword + "\n" +
                    "(만일 본인이 비밀번호를 요청한 적 없으면 4bitportal@gmail.com으로 연락주세요)";

            // mailService 통해서 메일 전송 (메일주소, 제목, 본문)
            mailService.sendEmail(address, msgSubject, msgText);

            return "요청하신 이메일 주소 " + address + "로 임시비밀번호를 보내드렸습니다";
        }
    }
}

