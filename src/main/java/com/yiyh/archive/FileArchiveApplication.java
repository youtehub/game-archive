package com.yiyh.archive;

import cn.hutool.extra.spring.SpringUtil;
import com.yiyh.archive.service.FF7ReArchiveService;
import com.yiyh.archive.util.ZipUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;


@SpringBootApplication
@CrossOrigin
@EnableScheduling
public class FileArchiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileArchiveApplication.class, args);
//        ApplicationContext context = SpringUtil.getApplicationContext();
//        FF7ReArchiveService ff7ReArchiveService = context.getBean(FF7ReArchiveService.class);
//        ZipUtil.generateFile(ff7ReArchiveService);
//        ff7ReArchiveService.cleanExpiredFile();
    }


}
