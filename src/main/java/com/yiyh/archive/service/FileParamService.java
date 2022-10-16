package com.yiyh.archive.service;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import com.yiyh.archive.entity.FilePathParam;
import com.yiyh.archive.util.ThreadLocalUtil;
import com.yiyh.archive.util.ZipUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 最终幻想7-重置版的游戏存档
 */
@Component
public class FileParamService {

    @Value("${datePattern}")
    private String datePattern;
    @Value("${number}")
    private Integer number;
    @Value("${suffix}")
    private String suffix;

    @Autowired
    private ArchiveService archiveService;

    @Value("${sourcePath}")
    private String sourcePath;
    @Value("${remotePath}")
    private String remotePath;
    @Value("${tempPath}")
    private String tempPath;
    @Value("${fileName}")
    private String fileName;


    public void createArchive() {
        FilePathParam pathParam = ThreadLocalUtil.get(ThreadLocalUtil.THREAD_LOCAL_KEY);
        if (ObjectUtil.isEmpty(pathParam)) {
//            getInputPath();
            pathParam = getGamePath(fileName);
            ThreadLocalUtil.set(ThreadLocalUtil.THREAD_LOCAL_KEY, pathParam);
        }
//        ZipUtil.generateFile(archiveService, pathParam);
        archiveService.zipFile(pathParam);
        archiveService.cleanExpiredFile(pathParam);
    }

    private Map getGameName() {
        return Dict.create().set("name", fileName);
    }

    private FilePathParam getGamePath(String gameName) {
        FilePathParam ff7re = new FilePathParam(sourcePath, remotePath, tempPath,
                fileName, datePattern, number, suffix);
        Dict dict = Dict.create()
                .set(fileName, ff7re);
        return (FilePathParam) dict.get(gameName);
    }


    private FilePathParam getInputPath() {
        FilePathParam pathParam = null;
        Map gameName = getGameName();
        Integer count = 1;
        Scanner scan = null;
        while (count < 4) {
            System.out.println("下列为可以运行的游戏名称：");
            gameName.values().forEach(System.out::println);
            System.out.println("请手动输入：");
            scan = new Scanner(System.in);
            if (scan.hasNext()) {
                String gameNameStr = scan.next().trim();
                System.out.println("输入的数据为：" + gameNameStr);
                pathParam = getGamePath(gameNameStr);
                if (ObjectUtil.isEmpty(pathParam)) {
                    System.out.println("游戏名称第" + count + "次输入错误，请重新输入");
                    count++;
                } else {
                    break;
                }
            }
        }
        scan.close();
        if (count > 3) {
            System.out.println("游戏名称输入错误超过3次，程序将自动结束");
            System.exit(0);
        }
        return pathParam;
    }

}

