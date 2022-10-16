package com.yiyh.archive.entity;


public class FilePathParam {

    private String sourcePath;

    private String remotePath;

    private String tempPath;

    private String fileName;

    private String datePattern;

    private Integer number;

    private String suffix;

    public FilePathParam() {
    }

    public FilePathParam(String sourcePath, String remotePath, String tempPath, String fileName, String datePattern, Integer number, String suffix) {
        this.sourcePath = sourcePath;
        this.remotePath = remotePath;
        this.tempPath = tempPath;
        this.fileName = fileName;
        this.datePattern = datePattern;
        this.number = number;
        this.suffix = suffix;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    public String getTempPath() {
        return tempPath;
    }

    public void setTempPath(String tempPath) {
        this.tempPath = tempPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDatePattern() {
        return datePattern;
    }

    public void setDatePattern(String datePattern) {
        this.datePattern = datePattern;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
