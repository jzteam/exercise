package cn.jzteam.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class HostUtil {

    /**
     * 获取本机内网ip
     * @return
     */
    private static String readLocalIp() {
        String inIp = null;

        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            NetworkInterface netInterface;
            while (allNetInterfaces.hasMoreElements()) {
                netInterface = allNetInterfaces.nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                InetAddress inetAddress = null;
                while (addresses.hasMoreElements()) {
                    inetAddress = addresses.nextElement();
                    if (inetAddress != null && inetAddress instanceof Inet4Address) {
                        String ip = inetAddress.getHostAddress();
                        if ("127.0.0.1".equals(ip) || "localhost".equals(ip)) {
                            continue;
                        }
                        inIp = ip;
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException("Read In-Network Ip fail, please check Network first!");
        }
        if (inIp == null) {
            throw new RuntimeException("Read In-Network Ip fail, please check In-Network first!");
        }
        return inIp;
    }

    public static void main(String[] args) {
        System.out.println(HostUtil.readLocalIp());
    }
}
