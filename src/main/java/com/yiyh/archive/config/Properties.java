package com.yiyh.archive.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 注入自定义配置前缀文件
 */
@Component
@PropertySource(value = "classpath:application.yml", encoding = "UTF-8")
@ConfigurationProperties(prefix = "com.delete-file")
public class Properties {
}

