package com.yiyh.archive.service;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.FileUtil;
import com.yiyh.archive.entity.ArchiveFile;
import com.yiyh.archive.entity.FilePathParam;
import com.yiyh.archive.filter.ExtFilter;
import com.yiyh.archive.util.ZipUtil;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 最终幻想7-重置版的游戏存档
 */
@Component
public class ArchiveService {


    public void cleanExpiredFile(FilePathParam pathParam) {
        List<ArchiveFile> fileList = findDeleteFile(pathParam);
        if (fileList.size() > pathParam.getNumber()) {
            List<ArchiveFile> deletedFileList = fileList.stream()
                    .sorted(Comparator.comparing(ArchiveFile::getCreateTime).reversed())
                    .skip(pathParam.getNumber())
                    .collect(Collectors.toList());
            for (ArchiveFile file : deletedFileList) {
                FileUtil.del(file.getFileName());
            }
        }
    }

    /**
     * 根据提供的地址，获取需要删除的文件
     *
     * @param pathParam
     * @return
     */
    private List<ArchiveFile> findDeleteFile(FilePathParam pathParam) {
        String[] zipFile = (new File(pathParam.getRemotePath())).list(new ExtFilter(pathParam.getSuffix()));
        List<ArchiveFile> fileList = new ArrayList<>();
        if (zipFile.length > 0) {
            fileList = Arrays.asList(zipFile).stream()
                    .map(file -> {
                        String deletedPath = pathParam.getRemotePath() + "\\" + file;
//                        LocalDateTime fileCreateTime = getFileCreateTime(deletedPath);
                        String createTimeStr = file.substring(file.indexOf("_") + 1, file.indexOf("."));
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pathParam.getDatePattern());
                        LocalDateTime createTime = LocalDateTime.parse(createTimeStr, dtf);
                        return new ArchiveFile(deletedPath, createTime);
                    })
                    .sorted(Comparator.comparing(ArchiveFile::getCreateTime))
                    .collect(Collectors.toList());
        }
        return fileList;
    }

    private static LocalDateTime getFileCreateTime(String filePath) {
        FileTime fileTime = null;
        try {
            fileTime = Files.readAttributes(Paths.get(filePath), BasicFileAttributes.class).creationTime();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LocalDateTime localDateTime = LocalDateTimeUtil.of(fileTime.toMillis(), TimeZone.getDefault());
        return localDateTime;
    }


    /**
     * 压缩，上传文件
     */
    public void zipFile(FilePathParam pathParam) {
        String timestamp = LocalDateTimeUtil.format(LocalDateTime.now(), pathParam.getDatePattern());
        String route = "\\" + pathParam.getFileName() + "_" + timestamp + "." + pathParam.getSuffix();
        String tempFile = pathParam.getTempPath() + route;
        String remoteFile = pathParam.getRemotePath() + route;
        try {
            Path path = Paths.get(pathParam.getTempPath());
            FileUtil.del(pathParam.getTempPath());
            Files.createDirectory(path);
            ZipUtil.zip(pathParam.getSourcePath(), tempFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        File modifyFile = new File(tempFile);
        boolean modified = modifyFile.setLastModified(new Date().getTime());
        FileUtil.copyFile(tempFile, remoteFile);
        FileUtil.del(pathParam.getTempPath());
        LocalDateTime fileCreateTime = getFileCreateTime(remoteFile);
    }
}

