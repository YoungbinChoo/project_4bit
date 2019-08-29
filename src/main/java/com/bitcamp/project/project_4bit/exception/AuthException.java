package com.bitcamp.project.project_4bit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


// Role 에 따라 권한을 체크해서 해당 권한이 없으면 예외처리
@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class AuthException extends RuntimeException{

    public AuthException(String message){
        super(message);
    }

    public AuthException(String message, Throwable t){
        super(message, t);
    }
}