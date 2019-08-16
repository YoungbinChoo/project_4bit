package com.bitcamp.project.project_4bit.entity;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "hw_file")
@DynamicInsert
public class HwFile implements Serializable {

    // 과제파일 고유번호
    @Id
    @Column(columnDefinition = "BIGINT", name = "HW_file_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hwFileId;

    // 과제파일 주소
    @Column(name = "HW_file_url")
    private String hwFileUrl;

    // 과제파일명
    @Column(name = "HW_file_name")
    private String hwFileName;

    // 과제파일 크기
    @Column(name = "HW_file_size")
    private int hwFileSize;

    // 과제제출 고유번호(FK)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "HW_article_id")
    private HwArticle hwArticle;


    /////////////////////////////////////////////////


    public Long getHwFileId() {
        return hwFileId;
    }

    public void setHwFileId(Long hwFileId) {
        this.hwFileId = hwFileId;
    }

    public String getHwFileUrl() {
        return hwFileUrl;
    }

    public void setHwFileUrl(String hwFileUrl) {
        this.hwFileUrl = hwFileUrl;
    }

    public String getHwFileName() {
        return hwFileName;
    }

    public void setHwFileName(String hwFileName) {
        this.hwFileName = hwFileName;
    }

    public int getHwFileSize() {
        return hwFileSize;
    }

    public void setHwFileSize(int hwFileSize) {
        this.hwFileSize = hwFileSize;
    }

    public HwArticle getHwArticle() {
        return hwArticle;
    }

    public void setHwArticle(HwArticle hwArticle) {
        this.hwArticle = hwArticle;
    }
}