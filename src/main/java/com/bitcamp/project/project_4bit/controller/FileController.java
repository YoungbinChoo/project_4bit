package com.bitcamp.project.project_4bit.controller;

import com.bitcamp.project.project_4bit.service.ArticleFileService;
import com.bitcamp.project.project_4bit.service.FileService;
import com.bitcamp.project.project_4bit.util.UploadFileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/fileupload")
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileService fileService;

    @Autowired
    private ArticleFileController articleFileController;

    @Autowired
    private ArticleFileService articleFileService;

    // 파일 한개를 업로드
    @PostMapping(value = "/file",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
        public UploadFileResponse uploadFile(
            @RequestParam("file")MultipartFile file,
            @RequestParam(name = "articleId") Long articleId,
            HttpServletRequest request,
            Principal principal){
        String replaceFileName = fileService.storeFile(file, request, principal);           // request : IP 때문에 넘김(누가 넘겼는지)

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/fileupload/files/")
                .path(replaceFileName)
                .toUriString();

        Long fileId = fileService.findFileId(replaceFileName).getFileId();


        articleFileController.createArticleFile(articleId,fileId);

        // header에 accept : application/json 해줘야 함
        return new UploadFileResponse(file.getOriginalFilename(), replaceFileName, fileDownloadUri, file.getContentType(),file.getSize());      // fileDownloadUri : 사용자가 다운로드할 URL
    }

    // 여러 파일을 업로드
    @PostMapping(path = "/files")
    public List<UploadFileResponse> uploadFiles(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam(name = "articleId") Long articleId,
            HttpServletRequest request,
            Principal principal){
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file, articleId, request, principal))
                .collect(Collectors.toList());
    }

    // 파일 다운로드
    @GetMapping("/files/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName){

        // Load file as Resource
        Resource resource = fileService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String extendName = null;
        try{
            extendName = fileService.retrieveFileContentType(fileName);
        }catch (Exception e){
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(extendName == null){
            extendName = "application/octet-stream";           // 확장명이 null 일때 application/octet-stream으로 세팅해준다.
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(extendName))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
