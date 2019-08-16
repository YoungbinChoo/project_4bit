package com.bitcamp.project.project_4bit.entity;


import com.bitcamp.project.project_4bit.util.BooleanToStringConverter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;

//board_manager 테이블
@Entity
@Table(name = "board_type_list")
@DynamicInsert
public class BoardTypeList implements Serializable {

    // PK : board_id  (게시판영문명)
    @Id
    @Column(columnDefinition = "VARCHAR(50)", name = "board_id", updatable = false, nullable = false)
    private String boardId;

    // board_name    (게시판한글명)
    @Column(name = "board_name")
    private String boardName;

    // isnotice       (공지표시유무)
    @Column(name = "isnotice")
    @Convert(converter = BooleanToStringConverter.class)
    private Boolean isnotice;

    @Column(name = "article_last_number")
    private int articleLastNumber;

    // FK : 제약이름 (constraint_name) from : constraint_define
    @OneToOne
    @JoinColumn(name = "constraint_name")
    private ConstraintDefine constraintDefine;

    // FK : class_id (반_고유번호) From : class_group
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "class_id")
    private ClassGroup classGroup;


    /////////////////////////////////////////////////////////////////////////////


    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public Boolean getIsnotice() {
        return isnotice;
    }

    public void setIsnotice(Boolean isnotice) {
        this.isnotice = isnotice;
    }

    public int getArticleLastNumber() {
        return articleLastNumber;
    }

    public void setArticleLastNumber(int articleLastNumber) {
        this.articleLastNumber = articleLastNumber;
    }

    public ConstraintDefine getConstraintDefine() {
        return constraintDefine;
    }

    public void setConstraintDefine(ConstraintDefine constraintDefine) {
        this.constraintDefine = constraintDefine;
    }

    public ClassGroup getClassGroup() {
        return classGroup;
    }

    public void setClassGroup(ClassGroup classGroup) {
        this.classGroup = classGroup;
    }
}
