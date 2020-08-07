package com.x.shop.plugin.file;

import com.x.shop.common.FileInfo;
import com.x.shop.common.Setting;
import com.x.shop.plugin.StoragePlugin;
import com.x.shop.util.ContextPathUtils;
import com.x.shop.util.SettingUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Plugin - 本地文件存储
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Component("filePlugin")
public class FilePlugin extends StoragePlugin {

    @Override
    public String getName() {
        return "本地文件存储";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String getAuthor() {
        return "X";
    }

    @Override
    public String getSiteUrl() {
        return "http://www.x.com";
    }

    @Override
    public String getInstallUrl() {
        return null;
    }

    @Override
    public String getUninstallUrl() {
        return null;
    }

    @Override
    public String getSettingUrl() {
        return "file/setting.html";
    }


    @Override
    public void upload(String path, File file, String contentType) {
        File destFile = new File(ContextPathUtils.getStaticPath(path));
        try {
            FileUtils.moveFile(file, destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getUrl(String path) {
        return base + path;
    }

    @Override
    public List<FileInfo> browser(String path) {
        List<FileInfo> fileInfos = new ArrayList<>();
        File directory = new File(ContextPathUtils.getStaticPath(path));
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    FileInfo fileInfo = new FileInfo();
                    fileInfo.setName(file.getName());
                    fileInfo.setUrl(base + path + file.getName());
                    fileInfo.setIsDirectory(file.isDirectory());
                    fileInfo.setSize(file.length());
                    fileInfo.setLastModified(new Date(file.lastModified()));
                    fileInfos.add(fileInfo);
                }
            }
        }
        return fileInfos;
    }

}