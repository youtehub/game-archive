package com.yiyh.archive.service;

import java.io.File;
import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.FileUtil;
import com.yiyh.archive.entity.DeleteFile;
import com.yiyh.archive.filter.ExtFilter;
import com.yiyh.archive.util.ZipUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 最终幻想7-重置版的游戏存档
 */
@Component
public class FF7ReArchiveService {

    @Value("${ff7re_sourcePath}")
    private String sourcePath;
    @Value("${ff7re_remotePath}")
    private String remotePath;
    @Value("${ff7re_tempPath}")
    private String tempPath;
    @Value("${ff7re_fileName}")
    private String fileName;

    @Value("${datePattern}")
    private String datePattern;
    @Value("${number}")
    private Integer number;
    @Value("${suffix}")
    private String suffix;

    public void cleanExpiredFile() {
        List<DeleteFile> fileList = findDeleteFile(remotePath, suffix);
        if (fileList.size() > number) {
            List<DeleteFile> deletedFileList = fileList.stream()
                    .sorted(Comparator.comparing(DeleteFile::getCreateTime).reversed())
                    .skip(number)
                    .collect(Collectors.toList());
            for (DeleteFile file : deletedFileList) {
                FileUtil.del(file.getFileName());
            }
        }
    }

    /**
     * 根据提供的地址，获取需要删除的文件
     *
     * @param filePath
     * @return
     */
    private List<DeleteFile> findDeleteFile(String filePath, String suffix) {
        String[] zipFile = (new File(filePath)).list(new ExtFilter(suffix));
        List<DeleteFile> fileList = new ArrayList<>();
        if (zipFile.length > 0) {
            fileList = Arrays.asList(zipFile).stream()
                    .map(file -> {
                        String deletedPath = filePath + "\\" + file;
                        String createTimeStr = file.substring(file.indexOf("_") + 1, file.indexOf("."));
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(datePattern);
                        LocalDateTime createTime = LocalDateTime.parse(createTimeStr, dtf);
                        return new DeleteFile(deletedPath, createTime);
                    })
                    .sorted(Comparator.comparing(DeleteFile::getCreateTime))
                    .collect(Collectors.toList());
        }
        return fileList;
    }

    /**
     * 压缩，上传文件
     */
    public void zipFile() {
        String timestamp = LocalDateTimeUtil.format(LocalDateTime.now(), datePattern);
        String path = "\\" + fileName + "_" + timestamp + "." + suffix;
        String tempFile = tempPath + path;
        String remoteFile = remotePath + path;
        try {
            ZipUtil.zip(sourcePath, tempFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        FileUtil.copyFile(tempFile, remoteFile);
        FileUtil.del(tempFile);
    }
}

