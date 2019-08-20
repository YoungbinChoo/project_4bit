package com.bitcamp.project.project_4bit.service;

import com.bitcamp.project.project_4bit.entity.File;
import com.bitcamp.project.project_4bit.entity.User;
import com.bitcamp.project.project_4bit.exception.FileException;
import com.bitcamp.project.project_4bit.exception.FileNotFoundException;
import com.bitcamp.project.project_4bit.repository.FileRepository;
import com.bitcamp.project.project_4bit.util.FileProperties;
import com.bitcamp.project.project_4bit.util.HashingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDateTime;

@Service
public class FileService {

    private final Path fileLocation;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private LocalUserDetailsService userDetailsService;

    @Autowired
    public FileService(FileProperties fileProperties){
        this.fileLocation = Paths.get(fileProperties.getUploadDir())
                .toAbsolutePath().normalize();
        try{
            Files.createDirectories(this.fileLocation);
        }catch(Exception e){
            throw new FileException("Could not create the directory where the uploaded files will be stored.", e);
        }
    }

    public String storeFile(MultipartFile file, HttpServletRequest request, Principal principal){
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try{
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")){
                throw new FileException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            // Move file to the target location
            Path targetLocation = this.fileLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // 파일 명 변경 ( 동일한 이름을 가진 파일이 들어오더라도 중복되지 않게 바꿔주기 위함)
            HashingUtil hashingUtil = new HashingUtil();

            // 동일한 파일이라도 시간에 따라 파일명이 바뀌게 함
            String replaceFileName = hashingUtil.sha256Encoding(fileName + LocalDateTime.now());
            Files.move(targetLocation, targetLocation.resolveSibling(replaceFileName));

            // 파일명 변경 후 DB에 매핑
            User user = (User) userDetailsService.loadUserByUsername(principal.getName());
            File fileEntity = new File();
            fileEntity.setFileOriginName(fileName);
            fileEntity.setFileName(replaceFileName);
            fileEntity.setFileSize(file.getSize());
            fileEntity.setFileUploadIp(request.getRemoteAddr());

            fileEntity.setFileContentType(file.getContentType());
            fileEntity.setUser(user);

            fileRepository.save(fileEntity);

            return replaceFileName;

        }catch(Exception e){
            throw new FileNotFoundException("File not found" + fileName);
//            e.printStackTrace();
        }
//        return null;
    }

    public Resource loadFileAsResource(String fileName){
        try{
            Path filePath = this.fileLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()){
                return resource;
            } else{
                throw new FileNotFoundException("File not found " + fileName);
//                return null;
            }
        }catch(MalformedURLException e){
            throw new FileNotFoundException("File not found " + fileName, e);
//            e.printStackTrace();
        }
//        return null;
    }

    public String retrieveFileContentType(String fileName) {
        String contentType = fileRepository.findByFileName(fileName).getFileContentType();
        return contentType;
    }
}
