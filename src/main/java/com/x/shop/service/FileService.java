package com.x.shop.service;

import com.x.shop.common.FileInfo;
import com.x.shop.common.FileInfo.FileType;
import com.x.shop.common.FileInfo.OrderType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Service - 文件
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
public interface FileService {

    /**
     * 文件验证
     *
     * @param fileType      文件类型
     * @param multipartFile 上传文件
     * @return 文件验证是否通过
     */
    boolean isValid(FileType fileType, MultipartFile multipartFile);

    /**
     * 文件上传
     *
     * @param fileType      文件类型
     * @param multipartFile 上传文件
     * @param async         是否异步
     * @return 访问URL
     */
    String upload(FileType fileType, MultipartFile multipartFile, boolean async);

    /**
     * 文件上传(异步)
     *
     * @param fileType      文件类型
     * @param multipartFile 上传文件
     * @return 访问URL
     */
    String upload(FileType fileType, MultipartFile multipartFile);

    /**
     * 文件上传至本地
     *
     * @param fileType      文件类型
     * @param multipartFile 上传文件
     * @return 路径
     */
    String uploadLocal(FileType fileType, MultipartFile multipartFile);

    /**
     * 文件浏览
     *
     * @param path      浏览路径
     * @param fileType  文件类型
     * @param orderType 排序类型
     * @return 文件信息
     */
    List<FileInfo> browser(String path, FileType fileType, OrderType orderType);

}