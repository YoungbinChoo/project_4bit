package com.bitcamp.project.project_4bit.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

//게시물 파일 테이블
@Entity
@Table(name = "file")
@DynamicInsert
@DynamicUpdate
public class File implements Serializable {

    // PK : file_id 파일_고유번호     AutoIncrement를 사용
    @Id
    @Column(columnDefinition = "BIGINT", name = "file_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    // 원래 파일명
    @Column(name = "file_origin_name")
    private String fileOriginName;

    // 바뀐 파일명
    @Column(name = "file_name")
    private String fileName;

    // file_size 파일크기
    @Column(name = "file_size")
    private Long fileSize;

    // 업로드한 곳의 IP
    @Column(name = "file_upload_ip")
    private String fileUploadIp;

    // 파일의 확장명
    @Column(name = "file_extend_name")
    private String fileExtendName;

    // user_id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

//    @ManyToMany(mappedBy = "File")
//    private Set<Article> articles;


    //////////////////////////////////////////////////////////////////////////////////////////////


    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFileOriginName() {
        return fileOriginName;
    }

    public void setFileOriginName(String fileOriginName) {
        this.fileOriginName = fileOriginName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileUploadIp() {
        return fileUploadIp;
    }

    public void setFileUploadIp(String fileUploadIp) {
        this.fileUploadIp = fileUploadIp;
    }

    public String getFileExtendName() {
        return fileExtendName;
    }

    public void setFileExtendName(String fileExtendName) {
        this.fileExtendName = fileExtendName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }




}
