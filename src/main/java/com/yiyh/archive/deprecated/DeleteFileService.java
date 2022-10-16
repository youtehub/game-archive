package com.yiyh.archive.deprecated;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.FileUtil;
import com.yiyh.archive.entity.ArchiveFile;
import com.yiyh.archive.filter.ExtFilter;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Deprecated
public class DeleteFileService {

    @Value("${remotePath}")
    private String filePath;
    @Value("${suffix}")
    private String suffix;

    @Value("${fileName}")
    private String fileName;

    public void cleanExpiredFile() {
        List<ArchiveFile> fileList = findDeleteFile(filePath, suffix);
        if (fileList.size() > 10) {
            List<ArchiveFile> deletedFileList = fileList.stream()
                    .sorted(Comparator.comparing(ArchiveFile::getCreateTime)
                            .reversed())
                    .skip(10)
                    .collect(Collectors.toList());
            for (ArchiveFile file : deletedFileList) {
                DeleteFileService.delPathFile(new File(file.getFileName()));
            }
            List<ArchiveFile> renameList = findDeleteFile(filePath, suffix);
            //对文件进行重命名
            renameFile(filePath, renameList);
            //清除重命名前的文件
            cleanFile(renameList);

        }
    }


    /**
     * 对文件进行重命名
     *
     * @param renameList 重命名文件集合
     */
    private void cleanFile(List<ArchiveFile> renameList) {
        for (int i = 0; i < renameList.size(); i++) {
            String fileName = renameList.get(i).getFileName();
            File file = new File(fileName);
            delPathFile(file);
        }
    }

    /**
     * 对文件进行重命名
     *
     * @param filePath   文件夹路径
     * @param renameList 重命名文件集合
     */
    private void renameFile(String filePath, List<ArchiveFile> renameList) {
        for (int i = 0; i < renameList.size(); i++) {
            try {
                Thread.sleep(10000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            ArchiveFile archiveFile = renameList.get(i);
            File temp = new File(archiveFile.getFileName());
            String createTime = LocalDateTimeUtil.format(archiveFile.getCreateTime(), "yyyy-MM-dd_HH_mm_ss");
            String timestamp = LocalDateTimeUtil.format(LocalDateTime.now(), "yyyyMMddHHmmss");
            if (temp.isFile()) {
                String oldFileName = temp.getName();
                String suffix = oldFileName.substring(oldFileName.lastIndexOf(".") + 1);
                String newFileName = filePath + "\\" + fileName + "-" + createTime + "_" + timestamp + "." + suffix;
                FileUtil.copyFile(temp.getAbsolutePath(), newFileName);
            }
        }
    }

    /**
     * 根据提供的地址，获取需要删除的文件
     *
     * @param filePath
     * @return
     */
    private List<ArchiveFile> findDeleteFile(String filePath, String suffix) {
        String[] zipFile = (new File(filePath)).list(new ExtFilter(suffix));
        List<ArchiveFile> fileList = new ArrayList<>();
        if (zipFile.length > 0) {
            fileList = Arrays.asList(zipFile).stream().map(file -> {
                        File deletedFile = new File(filePath + "\\" + file);
                        LocalDateTime createTime = DeleteFileService.getFileCreateTime(deletedFile.getAbsolutePath());
                        return new ArchiveFile(deletedFile.getAbsolutePath(), createTime);
                    })
                    .sorted(Comparator.comparing(ArchiveFile::getCreateTime))
                    .collect(Collectors.toList());
        }
        return fileList;
    }


    /**
     * 获取每个zip文件的创建时间
     *
     * @param filePath
     * @return
     */
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

    //清理文件
    private static void delPathFile(File file) {
        //清理文件
        file.delete();
        System.out.println(file.getName() + "已清理!!!");
    }
}
