package com.x.shop.service.impl;

import com.x.shop.common.FileInfo;
import com.x.shop.common.Setting;
import com.x.shop.plugin.StoragePlugin;
import com.x.shop.service.FileService;
import com.x.shop.service.PluginService;
import com.x.shop.util.ContextPathUtils;
import com.x.shop.util.FreemarkerUtils;
import com.x.shop.util.SettingUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;

/**
 * Service - 文件
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Service("fileServiceImpl")
public class FileServiceImpl implements FileService {

    @Resource(name = "taskExecutor")
    private TaskExecutor taskExecutor;
    @Resource(name = "pluginServiceImpl")
    private PluginService pluginService;

    /**
     * 添加上传任务
     *
     * @param storagePlugin 存储插件
     * @param path          上传路径
     * @param tempFile      临时文件
     * @param contentType   文件类型
     */
    private void addTask(final StoragePlugin storagePlugin, final String path, final File tempFile, final String contentType) {
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    storagePlugin.upload(path, tempFile, contentType);
                } finally {
                    FileUtils.deleteQuietly(tempFile);
                }
            }
        });
    }

    @Override
    public boolean isValid(FileInfo.FileType fileType, MultipartFile multipartFile) {
        if (multipartFile == null) {
            return false;
        }
        Setting setting = SettingUtils.get();
        if (setting.getUploadMaxSize() != null && setting.getUploadMaxSize() != 0 && multipartFile.getSize() > setting.getUploadMaxSize() * 1024L * 1024L) {
            return false;
        }
        String[] uploadExtensions;
        if (fileType == FileInfo.FileType.flash) {
            uploadExtensions = setting.getUploadFlashExtensions();
        } else if (fileType == FileInfo.FileType.media) {
            uploadExtensions = setting.getUploadMediaExtensions();
        } else if (fileType == FileInfo.FileType.file) {
            uploadExtensions = setting.getUploadFileExtensions();
        } else {
            uploadExtensions = setting.getUploadImageExtensions();
        }
        if (ArrayUtils.isNotEmpty(uploadExtensions)) {
            return FilenameUtils.isExtension(multipartFile.getOriginalFilename(), uploadExtensions);
        }
        return false;
    }

    @Override
    public String upload(FileInfo.FileType fileType, MultipartFile multipartFile, boolean async) {
        if (multipartFile == null) {
            return null;
        }
        Setting setting = SettingUtils.get();
        String uploadPath;
        if (fileType == FileInfo.FileType.flash) {
            uploadPath = setting.getFlashUploadPath();
        } else if (fileType == FileInfo.FileType.media) {
            uploadPath = setting.getMediaUploadPath();
        } else if (fileType == FileInfo.FileType.file) {
            uploadPath = setting.getFileUploadPath();
        } else {
            uploadPath = setting.getImageUploadPath();
        }
        try {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("uuid", UUID.randomUUID().toString());
            String path = FreemarkerUtils.process(uploadPath, model);
            String destPath = path + UUID.randomUUID() + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());

            for (StoragePlugin storagePlugin : pluginService.getStoragePlugins(true)) {
                File tempFile = new File(System.getProperty("java.io.tmpdir") + "/upload_" + UUID.randomUUID() + ".tmp");
                if (!tempFile.getParentFile().exists()) {
                    tempFile.getParentFile().mkdirs();
                }
                multipartFile.transferTo(tempFile);
                if (async) {
                    addTask(storagePlugin, destPath, tempFile, multipartFile.getContentType());
                } else {
                    try {
                        storagePlugin.upload(destPath, tempFile, multipartFile.getContentType());
                    } finally {
                        FileUtils.deleteQuietly(tempFile);
                    }
                }
                return storagePlugin.getUrl(destPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String upload(FileInfo.FileType fileType, MultipartFile multipartFile) {
        return upload(fileType, multipartFile, false);
    }

    @Override
    public String uploadLocal(FileInfo.FileType fileType, MultipartFile multipartFile) {
        if (multipartFile == null) {
            return null;
        }
        Setting setting = SettingUtils.get();
        String uploadPath;
        if (fileType == FileInfo.FileType.flash) {
            uploadPath = setting.getFlashUploadPath();
        } else if (fileType == FileInfo.FileType.media) {
            uploadPath = setting.getMediaUploadPath();
        } else if (fileType == FileInfo.FileType.file) {
            uploadPath = setting.getFileUploadPath();
        } else {
            uploadPath = setting.getImageUploadPath();
        }
        try {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("uuid", UUID.randomUUID().toString());
            String path = FreemarkerUtils.process(uploadPath, model);
            String destPath = path + UUID.randomUUID() + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
            File destFile = new File(ContextPathUtils.getStaticPath(destPath));
            if (!destFile.getParentFile().exists()) {
                destFile.getParentFile().mkdirs();
            }
            multipartFile.transferTo(destFile);
            return destPath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<FileInfo> browser(String path, FileInfo.FileType fileType, FileInfo.OrderType orderType) {
        if (path != null) {
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            if (!path.endsWith("/")) {
                path += "/";
            }
        } else {
            path = "/";
        }
        Setting setting = SettingUtils.get();
        String uploadPath;
        if (fileType == FileInfo.FileType.flash) {
            uploadPath = setting.getFlashUploadPath();
        } else if (fileType == FileInfo.FileType.media) {
            uploadPath = setting.getMediaUploadPath();
        } else if (fileType == FileInfo.FileType.file) {
            uploadPath = setting.getFileUploadPath();
        } else {
            uploadPath = setting.getImageUploadPath();
        }
        String browsePath = StringUtils.substringBefore(uploadPath, "${");
        browsePath = StringUtils.substringBeforeLast(browsePath, "/") + path;

        List<FileInfo> fileInfos = new ArrayList<FileInfo>();
        if (browsePath.contains("..")) {
            return fileInfos;
        }
        for (StoragePlugin storagePlugin : pluginService.getStoragePlugins(true)) {
            fileInfos = storagePlugin.browser(browsePath);
            break;
        }
        if (orderType == FileInfo.OrderType.size) {
            fileInfos.sort(new SizeComparator());
        } else if (orderType == FileInfo.OrderType.type) {
            fileInfos.sort(new TypeComparator());
        } else {
            fileInfos.sort(new NameComparator());
        }
        return fileInfos;
    }

    private class NameComparator implements Comparator<FileInfo> {
        @Override
        public int compare(FileInfo fileInfos1, FileInfo fileInfos2) {
            return new CompareToBuilder().append(!fileInfos1.getIsDirectory(), !fileInfos2.getIsDirectory()).append(fileInfos1.getName(), fileInfos2.getName()).toComparison();
        }
    }

    private class SizeComparator implements Comparator<FileInfo> {
        @Override
        public int compare(FileInfo fileInfos1, FileInfo fileInfos2) {
            return new CompareToBuilder().append(!fileInfos1.getIsDirectory(), !fileInfos2.getIsDirectory()).append(fileInfos1.getSize(), fileInfos2.getSize()).toComparison();
        }
    }

    private class TypeComparator implements Comparator<FileInfo> {
        @Override
        public int compare(FileInfo fileInfos1, FileInfo fileInfos2) {
            return new CompareToBuilder().append(!fileInfos1.getIsDirectory(), !fileInfos2.getIsDirectory()).append(FilenameUtils.getExtension(fileInfos1.getName()), FilenameUtils.getExtension(fileInfos2.getName())).toComparison();
        }
    }

}