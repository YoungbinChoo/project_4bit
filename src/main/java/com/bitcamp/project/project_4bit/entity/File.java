package com.bitcamp.project.project_4bit.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

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

    // file_origin_name
    @Column(name = "file_origin_name")
    private String fileOriginName;

    // file_name 파일명
    @Column(name = "file_name")
    private String fileName;

    // file_size 파일크기
    @Column(name = "file_size")
    private Long fileSize;       // int 형으로 해야될지 Integer로 해야될지 고민


//    @Column(name = "file_reg_date", updatable = false, insertable = false)
//    private Date fileRegDate;

    @Column(name = "file_upload_ip")
    private String fileUploadIp;

    @Column(name = "file_content_type")
    private String fileContentType;

    // user_id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

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

//    public Date getFileRegDate() {
//        return fileRegDate;
//    }
//
//    public void setFileRegDate(Date fileRegDate) {
//        this.fileRegDate = fileRegDate;
//    }

    public String getFileUploadIp() {
        return fileUploadIp;
    }

    public void setFileUploadIp(String fileUploadIp) {
        this.fileUploadIp = fileUploadIp;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
