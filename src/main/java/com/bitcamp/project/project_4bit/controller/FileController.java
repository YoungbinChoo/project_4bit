package com.bitcamp.project.project_4bit.controller;

import com.bitcamp.project.project_4bit.service.FileService;
import com.bitcamp.project.project_4bit.util.UploadFileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServlet;
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

    @PostMapping(value = "/file",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
        public UploadFileResponse uploadFile(@RequestParam("file")MultipartFile file, HttpServletRequest request, Principal principal){
        String replaceFileName = fileService.storeFile(file, request, principal);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/fileupload/files/")
                .path(replaceFileName)
                .toUriString();

        // header에 accept : application/json 해줘야 함
        return new UploadFileResponse(file.getOriginalFilename(), replaceFileName, fileDownloadUri, file.getContentType(),file.getSize());
    }

    @PostMapping(value = "/files")
    public List<UploadFileResponse> uploadFiles(@RequestParam("files") MultipartFile[] files, HttpServletRequest request, Principal principal){
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file, request, principal))
                .collect(Collectors.toList());
    }

    @GetMapping("/files/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName){

        // Load file as Resource
        Resource resource = fileService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try{
            contentType = fileService.retrieveFileContentType(fileName);
        }catch (Exception e){
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null){
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }








}
