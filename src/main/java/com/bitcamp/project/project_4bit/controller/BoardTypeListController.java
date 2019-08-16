package com.bitcamp.project.project_4bit.controller;

import com.bitcamp.project.project_4bit.entity.Article;
import com.bitcamp.project.project_4bit.service.ArticleService;
import com.bitcamp.project.project_4bit.service.BoardTypeListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boardlist")
public class BoardTypeListController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private BoardTypeListService boardTypeListService;

}
