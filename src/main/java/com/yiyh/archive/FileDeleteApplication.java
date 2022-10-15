package com.yiyh.archive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;


@SpringBootApplication
@CrossOrigin
@EnableScheduling
public class FileDeleteApplication {


    public static void main(String[] args) {
        SpringApplication.run(FileDeleteApplication.class, args);
//        ApplicationContext context = SpringUtil.getApplicationContext();
//        deleteFileService = context.getBean(DeleteFileService.class);
//        deleteFileService.cleanExpiredFile();
    }
}
