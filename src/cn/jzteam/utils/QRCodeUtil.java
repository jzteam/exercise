package cn.jzteam.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class QRCodeUtil {

    public final static String BASE64_HEAD = "data:image/png;base64,";

    /**
     * 生成二维码图像，返回base64，包含文件头，默认大小200*200
     *
     * @param content
     * @return
     */
    public static String generateToBase64Str(String content){
        // 默认宽高都是200
        return generateToBase64Str(content,200,200);
    }

    /**
     * 生成二维码图像，返回base64，包含文件头。
     * base64一行超过76字符会自动添加换行（win-\r\n;mac-\r;linux-\n），可以去掉换行符方便使用
     *
     * @param content
     * @param width
     * @param height
     * @return
     */
    public static String generateToBase64Str(String content,int width,int height){
        String format = "png";// 图像类型
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        try {
            // 生成矩阵
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
                    BarcodeFormat.QR_CODE, width, height, hints);
            BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
            // 新建流
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            // 利用ImageIO类提供的write方法，将bit以png图片的数据模式写入流。
            ImageIO.write(image, format, os);
            // 从流中获取数据数组
            byte b[] = os.toByteArray();
            return BASE64_HEAD + new String(Base64.getEncoder().encode(b)).replaceAll("\n", "");
        } catch (Exception e) {
            System.out.println("exception ...");
        }
        return null;
    }

    /**
     * 识别base64图片中的内容，自动去掉文件头
     *
     * @param base64Str
     * @return
     */
    public static String parseFromBase64Str(String base64Str){
        if(base64Str.startsWith("data:")){
            // 包含文件头，先去掉文件头
            base64Str = base64Str.substring(base64Str.indexOf(",")+1,base64Str.length());
        }
        try {
            final byte[] bytes = Base64.getDecoder().decode(base64Str);
            ByteArrayInputStream is = new ByteArrayInputStream(bytes);
            LuminanceSource source = new BufferedImageLuminanceSource(ImageIO.read(is));
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
            Map<DecodeHintType, Object> hints = new HashMap<>();
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            // 对图像进行解码
            Result result = new MultiFormatReader().decode(binaryBitmap, hints);
            return result.getText();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成二维码，写入到文件中，自动写入文件头"data:image/png;base64,"
     *
     * @param filePath
     * @param content
     * @param width
     * @param height
     * @return
     */
    public static String generateToFile(String filePath, String content,int width,int height){
        String format = "png";// 图像类型
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        try {
            // 生成矩阵
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
                    BarcodeFormat.QR_CODE, width, height, hints);
            Path path = FileSystems.getDefault().getPath(filePath);
            MatrixToImageWriter.writeToPath(bitMatrix, format, path);// 输出图像
        } catch (Exception e) {
            System.out.println("exception ...");
        }
        return null;
    }

    /**
     * 能识别图片中的二维码
     *
     * @param filePath
     * @return
     */
    public static String parseFromFile(String filePath){
        try {
            LuminanceSource source = new BufferedImageLuminanceSource(ImageIO.read(new File(filePath)));
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
            Map<DecodeHintType, Object> hints = new HashMap<>();
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            // 对图像进行解码
            Result result = new MultiFormatReader().decode(binaryBitmap, hints);
            return result.getText();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String url = "https://www.binance.com/?ref=10926868";
        url = "https://api.megvii.com/faceid/lite/do";
        String filePath = "/Users/oker/Documents/test/zxingtest.png";
        String filePath1 = "/Users/oker/Documents/test/testzxing.png";
        String base64Str = generateToBase64Str(url);
        System.out.println("base64Str = "+base64Str);
        //url= parseFromBase64Str(base64Str);
        System.out.println("解析得到：url="+url);

        //generateToFile(filePath,url,200,200);
        System.out.println("写入完成");
        System.out.println("解析结果："+parseFromFile(filePath));
    }
}
