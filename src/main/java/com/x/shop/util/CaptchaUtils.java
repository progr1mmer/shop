package com.x.shop.util;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @author progr1mmer
 * @date Created on 2020/2/23
 */
public class CaptchaUtils {

    private static final int WIDTH = 80;
    private static final int HEIGHT = 26;
    /**
     * 定义验证码图片的背景色
     */
    private static final Color BG_COLOR = new Color(255, 255, 255);
    /**
     * 随机字符
     */
    private static final String CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    /**
     * 随机字体
     */
    private static final String[] FONT_NAMES = {"nyala", "Arial", "Bell", "Bell MT", "Credit", "valley", "Impact"};

    private CaptchaUtils() {

    }

    /**
     * 创建BufferedImage对象
     *
     * @return
     */
    public static BufferedImage createImage(String captchaId) {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        g2.setColor(BG_COLOR);
        g2.fillRect(0, 0, WIDTH, HEIGHT);
        Random random = new Random();
        StringBuilder captcha = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            String s = randomChar(random) + "";
            captcha.append(s);
            g2.setColor(randomColor(random));
            g2.setFont(randomFont(random));
            float x = i * WIDTH * 1.0f / 4;
            g2.drawString(s, x, HEIGHT - 8);
        }
        drawLine(image, random);
        request.getSession().setAttribute(captchaId, captcha.toString());
        return image;
    }

    public static boolean validateResponseForID(String captchaId, String captcha) {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
        return captcha.equalsIgnoreCase((String) request.getSession().getAttribute(captchaId));
    }

    /**
     * 获取随机一个字符
     *
     * @return
     */
    private static char randomChar(Random random) {
        return CHARS.charAt(random.nextInt(CHARS.length()));
    }

    /**
     * 获取随机一个颜色
     *
     * @return
     */
    private static Color randomColor(Random random) {
        int red = random.nextInt(150);
        int green = random.nextInt(150);
        int blue = random.nextInt(150);
        return new Color(red, green, blue);
    }

    /**
     * 获取随机一个字体
     *
     * @return
     */
    private static Font randomFont(Random random) {
        String name = FONT_NAMES[random.nextInt(FONT_NAMES.length)];
        int style = random.nextInt(3);
        int size = random.nextInt(4) + 14;
        return new Font(name, style, size);
    }

    /**
     * 绘制干扰线
     */
    private static void drawLine(BufferedImage image, Random random) {
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        int num = 3;
        for (int i = 0; i < num; i++) {
            int x1 = random.nextInt(WIDTH);
            int y1 = random.nextInt(HEIGHT);
            int x2 = random.nextInt(WIDTH);
            int y2 = random.nextInt(HEIGHT);
            g2.setColor(randomColor(random));
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawLine(x1, y1, x2, y2);
        }
    }

}
