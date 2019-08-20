package com.bitcamp.project.project_4bit.util;

import org.springframework.boot.context.properties.ConfigurationProperties;


// file 이라는 properties 를 찾는것.
@ConfigurationProperties(prefix = "file")
public class FileProperties {
    private String uploadDir;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
