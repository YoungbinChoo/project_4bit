package com.bitcamp.project.project_4bit.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "roadmap")
public class Roadmap implements Serializable {

    @Id
    @Column(columnDefinition = "INT", name = "roadmap_stage_no", updatable = false, nullable = false)
    private Integer roadmapStageNo;

    @Column(name = "roadmap_subject")
    private String roadmapSubject;

    @Column(name = "roadmap_chapter")
    private String roadmapChapter;

    public Integer getRoadmapStageNo() {
        return roadmapStageNo;
    }

    public void setRoadmapStageNo(Integer roadmapStageNo) {
        this.roadmapStageNo = roadmapStageNo;
    }

    public String getRoadmapSubject() {
        return roadmapSubject;
    }

    public void setRoadmapSubject(String roadmapSubject) {
        this.roadmapSubject = roadmapSubject;
    }

    public String getRoadmapChapter() {
        return roadmapChapter;
    }

    public void setRoadmapChapter(String roadmapChapter) {
        this.roadmapChapter = roadmapChapter;
    }

}
