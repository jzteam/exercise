package cn.jzteam.utils;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.NetInfo;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.springframework.core.io.ClassPathResource;

import java.io.File;

public class SigarUtil {

    public final static Sigar sigar = initSigar();

    /**
     * sigar初始化
     * @return
     */
    private static Sigar initSigar() {
        try {
            File classPath = new ClassPathResource("sigar/.sigar_shellrc").getFile().getParentFile();

            String path = System.getProperty("java.library.path");
            if (OsCheck.getOperatingSystemType() == OsCheck.OSType.Windows) {
                path += ";" + classPath.getCanonicalPath();
            } else {
                path += ":" + classPath.getCanonicalPath();
            }
            System.setProperty("java.library.path", path);

            return new Sigar();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("init sigar error");
            return null;
        }
    }


    public static void main(String[] args) {
        try {
            CpuInfo infos[] = sigar.getCpuInfoList();
            final NetInfo netInfo = sigar.getNetInfo();
            System.out.println("ip="+netInfo.getHostName());
            for (int i = 0; i < infos.length; i++) {// 不管是单块CPU还是多CPU都适用
                CpuInfo info = infos[i];
                System.out.println("mhz=" + info.getMhz());// CPU的总量MHz
                System.out.println("vendor=" + info.getVendor());// 获得CPU的卖主，如：Intel
                System.out.println("model=" + info.getModel());// 获得CPU的类别，如：Celeron
                System.out.println("cache size=" + info.getCacheSize());// 缓冲存储器数量
            }
        } catch (SigarException e) {
            e.printStackTrace();
        }
    }
}
