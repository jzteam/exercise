package cn.jzteam.utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

public class ImageUtil {

    public static void mark(String imgPath, String outImgPath, String text, Font font, Color color, float alpha, Integer degree) {
        try {
            // 读取原图片信息
            File imgFile = null;
            Image img = null;
            if (imgPath != null) {
                imgFile = new File(imgPath);
            }
            if (imgFile != null && imgFile.exists() && imgFile.isFile() && imgFile.canRead()) {
                img = ImageIO.read(imgFile);
            }
            final BufferedImage bufImg = mark(img, text, font, color, alpha, degree);
            // 输出图片
            FileOutputStream outImgStream = new FileOutputStream(outImgPath);
            ImageIO.write(bufImg, "jpg", outImgStream);
            outImgStream.flush();
            outImgStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage mark(Image srcImg, String text, Font font, Color color, float alpha, Integer degree) {
        int imgWidth = srcImg.getWidth(null);
        int imgHeight = srcImg.getHeight(null);
        // 加水印
        BufferedImage resultImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);

        // 得到画笔对象
        Graphics2D g = resultImg.createGraphics();
        // 设置对线段的锯齿状边缘处理
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        g.drawImage(srcImg, 0, 0, resultImg.getWidth(), resultImg.getHeight(), null);


        g.setColor(color); //设置水印颜色
        g.setFont(font);              //设置字体
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));//设置水印文字透明度
        if (null != degree) {
            g.rotate(Math.toRadians(degree));//设置水印旋转
        }

        JLabel label = new JLabel(text);
        FontMetrics metrics = label.getFontMetrics(font);
        int width = metrics.stringWidth(label.getText());//文字水印的宽
        int rowsNumber = imgHeight / width; // 图片的高  除以  文字水印的宽 --> 打印的行数(以文字水印的宽为间隔)
        int columnsNumber = imgWidth / width; //图片的宽 除以 文字水印的宽  --> 每行打印的列数(以文字水印的宽为间隔)
        //防止图片太小而文字水印太长，所以至少打印一次
        if(rowsNumber < 1){
            rowsNumber = 1;
        }
        if(columnsNumber < 1){
            columnsNumber = 1;
        }
//        for(int j=0;j<rowsNumber;j++){
//            for(int i=0;i<columnsNumber;i++){
//                final int xr = (i ) * width;
//                final int yr = -(j)*width;
//                System.out.println("j="+j+", i="+i+", x="+xr+", y="+yr);
//                g.drawString(text, xr, yr);//画出水印,并设置水印位置
//            }
//        }
        g.drawString(text, 0, 50);//画出水印,并设置水印位置

        g.drawString(text, 0, 150);//画出水印,并设置水印位置
        g.dispose();

        return resultImg;
    }

    public static void main(String[] args) {
        String src = "/Users/oker/Downloads/img/日历/20e23d652072844a0567d6cf703c106f.jpg";
        String out = "/Users/oker/Downloads/img/mark_test.jpg";
        final Font font = new Font("Arial",Font.BOLD, 28);
        final Color color = Color.WHITE;

        final long start = System.currentTimeMillis();
        mark(src, out, " OKEX认证专用 ", font, color, 0.9f, 0);


        System.out.println("处理完成，耗时：" + (System.currentTimeMillis() - start)  + "ms");

    }
}
