package com.yiyh.archive;

import cn.hutool.extra.spring.SpringUtil;
import com.yiyh.archive.service.FileParamService;
import com.yiyh.archive.util.ThreadLocalUtil;
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
        ApplicationContext context = SpringUtil.getApplicationContext();
        FileParamService fileParamService = context.getBean(FileParamService.class);
        fileParamService.createArchive();
    }


}
