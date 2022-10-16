package com.yiyh.archive.entity;

import java.time.LocalDateTime;

/**
 * 需要删除的文件
 */
public class ArchiveFile {

    private String fileName;

    private LocalDateTime createTime;

    public ArchiveFile() {
    }

    public ArchiveFile(String fileName, LocalDateTime createTime) {
        this.fileName = fileName;
        this.createTime = createTime;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

}
