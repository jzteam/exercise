package cn.jzteam.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 工具类：字节数组的压缩/解压
 */
public class GzipUtil {

    /**
     * 压缩
     * @param val
     * @return
     * @throws IOException
     */
    public static byte[] gzip(byte[] val)throws IOException {
        if(val == null || val.length <= 0){
            return val;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream(val.length);
        GZIPOutputStream gos = null;
        try {
            gos = new GZIPOutputStream(bos);
            gos.write(val, 0, val.length);
            gos.finish();
            gos.flush();
            bos.flush();
            return bos.toByteArray();
        } finally {
            if (gos != null)
                gos.close();
            if (bos != null)
                bos.close();
        }
    }

    /**
     * 解压
     * @param buf
     * @return
     * @throws IOException
     */
    public static byte[] unGzip(byte[] buf)throws IOException {
        GZIPInputStream gzi = null;
        ByteArrayOutputStream bos = null;
        try {
            gzi = new GZIPInputStream(new ByteArrayInputStream(buf));
            bos = new ByteArrayOutputStream(buf.length);
            int count = 0;
            byte[] tmp = new byte[2048];
            while ((count = gzi.read(tmp)) != -1) {
                bos.write(tmp, 0, count);
            }
            return bos.toByteArray();
        } finally {
            if (bos != null) {
                bos.flush();
                bos.close();
            }
            if (gzi != null)
                gzi.close();
        }
    }

}
