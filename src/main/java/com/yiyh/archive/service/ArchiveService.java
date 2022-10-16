package com.yiyh.archive.service;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.FileUtil;
import com.yiyh.archive.entity.ArchiveFile;
import com.yiyh.archive.entity.FilePathParam;
import com.yiyh.archive.filter.ExtFilter;
import com.yiyh.archive.util.ZipUtil;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
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
        FileUtil.copyFile(tempFile, remoteFile);
        FileUtil.del(pathParam.getTempPath());
    }
}

