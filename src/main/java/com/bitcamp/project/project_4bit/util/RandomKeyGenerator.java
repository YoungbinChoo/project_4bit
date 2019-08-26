package com.bitcamp.project.project_4bit.util;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RandomKeyGenerator {

    // 난수생성 메서드 (int 자리수)
    public String createRandomKey(int size) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        int ranNum = 0;

        while (sb.length() < size) {
            ranNum = random.nextInt(75) + 48;  // 0~75사이의 숫자 생성, 거기에 +48 더해줌
            if ((ranNum >= 48 && ranNum <= 57) || (ranNum >= 65 && ranNum <= 90) || (ranNum >= 97 && ranNum <= 122)) {
                sb.append((char) ranNum);   // 생성된 랜덤 숫자를 char형으로 강제 형변환 후 sb에 넣어줌
            } else {
                continue;   // 아스키코드로 변환하기에 적절치 못한 숫자가 나올경우 랜덤숫자를 다시 생성
            }
        }
        return sb.toString().toLowerCase();     // 모인 StringBuffer를 소문자로 변경 후 리턴
    }
}
