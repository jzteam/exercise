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
            // 相对于原图，以左上角顶点为中心，将画布顺时针旋转degree度，旋转90度的时候，画布跟原图就没有交集了，水印是看不到的
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
        System.out.println("width="+width+", h="+imgHeight+", w="+imgWidth+", rowsNumber="+rowsNumber+", columnsNumber="+columnsNumber);
        // 行决定y，列决定x
        for(int i=0;i<rowsNumber+1;i++){
            for(int j=0;j<columnsNumber;j++){
                // 文字定位是以左下角的顶点来作为定位点的
                final int xr = j * (width + width/2); // 列 决定 x轴位置
                final int yr = i * width + width/2; // 行 决定 y轴位置（正向朝下），加上一个额外值，是为了让首行下移一些，不然看不见
                System.out.println("x="+xr+", y="+yr);
                // TODO xr和yr表示text的左下角顶点相对于画布的左上角顶点的坐标，x朝右为正方向，y朝下为正方向 !!!
                g.drawString(text, xr, yr);
            }
        }
//        g.drawString(text, 0, 50);//画出水印,并设置水印位置
//        g.drawString(text, 0, 150);//画出水印,并设置水印位置
        g.dispose();

        return resultImg;
    }

    public static void main(String[] args) {
        String src = "/Users/oker/Downloads/img/日历/20e23d652072844a0567d6cf703c106f.jpg";
        String out = "/Users/oker/Downloads/img/mark_test.jpg";
        final Font font = new Font("Arial",Font.BOLD, 28);
        final Color color = Color.WHITE;

        final long start = System.currentTimeMillis();
        mark(src, out, " OKEX认证专用 ", font, color, 0.9f, 80);


        System.out.println("处理完成，耗时：" + (System.currentTimeMillis() - start)  + "ms");

    }
}
