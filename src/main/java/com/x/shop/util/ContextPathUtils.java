package com.x.shop.util;

import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

/**
 * @author progr1mmer.
 * @date Created on 2020/2/13.
 */
public class ContextPathUtils {

    private static final ClassPathResource CLASS_PATH_RESOURCE = new ClassPathResource(".");

    /**
     * 不可实例化
     */
    private ContextPathUtils() {

    }

    public static String getClassPath() {
        try {
            return CLASS_PATH_RESOURCE.getURL().getPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getStaticPath(String path) {
        return getClassPath() + "static" + File.separator + path;
    }

}
