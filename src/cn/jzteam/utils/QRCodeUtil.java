package cn.jzteam.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class QRCodeUtil {

    public static String generateToBase64Str(String content){
        // 默认宽高都是200
        return generateToBase64Str(content,200,200);
    }

    /**
     * 生成二维码图像，返回base64
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
            return new BASE64Encoder().encode(b);
        } catch (Exception e) {
            System.out.println("exception ...");
        }
        return null;
    }

    public static String parseFromBase64Str(String base64Str){
        try {
            final byte[] bytes = new BASE64Decoder().decodeBuffer(base64Str);
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

    public static String parseFromFile(String filePath){
//        String filePath = "/Users/oker/work/test/zxing.png";
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
        String base64Str = generateToBase64Str("https://www.binance.com/?ref=10926868");
        System.out.println("base64Str = "+base64Str);
        final String url = parseFromBase64Str(base64Str);
        System.out.println("解析得到：url="+url);
    }
}
