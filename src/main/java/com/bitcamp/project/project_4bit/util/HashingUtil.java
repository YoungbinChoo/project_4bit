package com.bitcamp.project.project_4bit.util;

import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;


// 단방향 암호화 방식
public class HashingUtil {
    public String sha256Encoding(String original){
        return Hashing.sha256().hashString(original, StandardCharsets.UTF_8).toString();
    }
}
