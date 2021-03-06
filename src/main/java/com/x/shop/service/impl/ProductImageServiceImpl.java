package com.x.shop.service.impl;

import com.x.shop.common.Setting;
import com.x.shop.entity.ProductImage;
import com.x.shop.plugin.StoragePlugin;
import com.x.shop.service.ProductImageService;
import com.x.shop.util.ContextPathUtils;
import com.x.shop.util.FreemarkerUtils;
import com.x.shop.util.ImageUtils;
import com.x.shop.util.SettingUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;

/**
 * Service - 商品图片
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Service("productImageServiceImpl")
public class ProductImageServiceImpl implements ProductImageService {

    /**
     * 目标扩展名
     */
    private static final String DEST_EXTENSION = "jpg";
    /**
     * 目标文件类型
     */
    private static final String DEST_CONTENT_TYPE = "image/jpeg";

    @Resource(name = "taskExecutor")
    private TaskExecutor taskExecutor;
    @Resource
    private List<StoragePlugin> storagePlugins;

    /**
     * 添加图片处理任务
     *
     * @param sourcePath    原图片上传路径
     * @param largePath     图片文件(大)上传路径
     * @param mediumPath    图片文件(小)上传路径
     * @param thumbnailPath 图片文件(缩略)上传路径
     * @param tempFile      原临时文件
     * @param contentType   原文件类型
     */
    private void addTask(StoragePlugin storagePlugin, final String sourcePath, final String largePath, final String mediumPath, final String thumbnailPath, final File tempFile, final String contentType) {
        try {
            taskExecutor.execute(() -> {
                Setting setting = SettingUtils.get();
                String tempPath = System.getProperty("java.io.tmpdir");
                //File watermarkFile = new File(ContextPathUtils.getStaticPath(setting.getWatermarkImage()));
                File largeTempFile = new File(tempPath + "/upload_" + UUID.randomUUID() + "." + DEST_EXTENSION);
                File mediumTempFile = new File(tempPath + "/upload_" + UUID.randomUUID() + "." + DEST_EXTENSION);
                File thumbnailTempFile = new File(tempPath + "/upload_" + UUID.randomUUID() + "." + DEST_EXTENSION);
                try {
                    ImageUtils.zoom(tempFile, largeTempFile, setting.getLargeProductImageWidth(), setting.getLargeProductImageHeight());
                    //ImageUtils.addWatermark(largeTempFile, largeTempFile, watermarkFile, setting.getWatermarkPosition(), setting.getWatermarkAlpha());
                    ImageUtils.zoom(tempFile, mediumTempFile, setting.getMediumProductImageWidth(), setting.getMediumProductImageHeight());
                    //ImageUtils.addWatermark(mediumTempFile, mediumTempFile, watermarkFile, setting.getWatermarkPosition(), setting.getWatermarkAlpha());
                    ImageUtils.zoom(tempFile, thumbnailTempFile, setting.getThumbnailProductImageWidth(), setting.getThumbnailProductImageHeight());
                    storagePlugin.upload(sourcePath, tempFile, contentType);
                    storagePlugin.upload(largePath, largeTempFile, DEST_CONTENT_TYPE);
                    storagePlugin.upload(mediumPath, mediumTempFile, DEST_CONTENT_TYPE);
                    storagePlugin.upload(thumbnailPath, thumbnailTempFile, DEST_CONTENT_TYPE);
                } finally {
                    FileUtils.deleteQuietly(tempFile);
                    FileUtils.deleteQuietly(largeTempFile);
                    FileUtils.deleteQuietly(mediumTempFile);
                    FileUtils.deleteQuietly(thumbnailTempFile);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void build(ProductImage productImage) {
        MultipartFile multipartFile = productImage.getFile();
        if (multipartFile != null && !multipartFile.isEmpty()) {
            try {
                Setting setting = SettingUtils.get();
                Map<String, Object> model = new HashMap<String, Object>();
                model.put("uuid", UUID.randomUUID().toString());
                String uploadPath = FreemarkerUtils.process(setting.getImageUploadPath(), model);
                String uuid = UUID.randomUUID().toString();
                String sourcePath = uploadPath + uuid + "-source." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
                String largePath = uploadPath + uuid + "-large." + DEST_EXTENSION;
                String mediumPath = uploadPath + uuid + "-medium." + DEST_EXTENSION;
                String thumbnailPath = uploadPath + uuid + "-thumbnail." + DEST_EXTENSION;

                Collections.sort(storagePlugins);
                for (StoragePlugin storagePlugin : storagePlugins) {
                    if (storagePlugin.getIsEnabled()) {
                        File tempFile = new File(System.getProperty("java.io.tmpdir") + "/upload_" + UUID.randomUUID() + ".tmp");
                        if (!tempFile.getParentFile().exists()) {
                            tempFile.getParentFile().mkdirs();
                        }
                        multipartFile.transferTo(tempFile);
                        addTask(storagePlugin, sourcePath, largePath, mediumPath, thumbnailPath, tempFile, multipartFile.getContentType());
                        productImage.setSource(storagePlugin.getUrl(sourcePath));
                        productImage.setLarge(storagePlugin.getUrl(largePath));
                        productImage.setMedium(storagePlugin.getUrl(mediumPath));
                        productImage.setThumbnail(storagePlugin.getUrl(thumbnailPath));
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}