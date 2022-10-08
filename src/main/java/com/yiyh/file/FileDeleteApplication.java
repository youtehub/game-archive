package com.yiyh.file;

import com.yiyh.file.delete.DeleteFileService;
import com.yiyh.file.util.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;


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
