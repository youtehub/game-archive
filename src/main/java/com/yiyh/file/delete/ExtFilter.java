package com.yiyh.file.delete;

import java.io.File;
import java.io.FilenameFilter;

/**
 * 根据提供的后缀获取指定类型的文件
 */
public class ExtFilter implements FilenameFilter {
    private String ext;

    ExtFilter(String ext) {
        this.ext = ext;
    }

    public boolean accept(File dir, String name) {
        return name.toLowerCase().endsWith(ext);
    }

}
