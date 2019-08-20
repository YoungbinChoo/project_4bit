package com.bitcamp.project.project_4bit.util;

public class UploadFileResponse {

    private String replaceFileName;

    private String originFileName;

    private String fileDownloadUri;

    private String fileType;

    private long size;


    public String getReplaceFileName() {
        return replaceFileName;
    }

    public void setReplaceFileName(String replaceFileName) {
        this.replaceFileName = replaceFileName;
    }

    public String getOriginFileName() {
        return originFileName;
    }

    public void setOriginFileName(String originFileName) {
        this.originFileName = originFileName;
    }

    public String getFileDownloadUri() {
        return fileDownloadUri;
    }

    public void setFileDownloadUri(String fileDownloadUri) {
        this.fileDownloadUri = fileDownloadUri;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public UploadFileResponse(String originFileName, String replaceFileName, String fileDownloadUri, String fileType, long size) {
        this.originFileName = originFileName;
        this.replaceFileName = replaceFileName;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.size = size;
    }
}
