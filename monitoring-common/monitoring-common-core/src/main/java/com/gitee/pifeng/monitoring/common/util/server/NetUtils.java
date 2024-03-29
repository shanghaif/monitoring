package com.gitee.pifeng.monitoring.common.util.server;

import cn.hutool.core.net.NetUtil;
import com.gitee.pifeng.monitoring.common.domain.server.NetDomain;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.common.util.server.sigar.NetInterfaceUtils;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hyperic.sigar.SigarException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.List;

/**
 * <p>
 * 网络工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/12 11:20
 */
@Slf4j
public class NetUtils {

    /**
     * <p>
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2021/1/14 9:58
     */
    private NetUtils() {
    }

    /**
     * <p>
     * 获取本机MAC地址
     * </p>
     *
     * @return MAC地址
     * @throws NetException   获取网络信息异常：获取本机MAC地址异常！
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020/9/15 12:35
     */
    public static String getLocalMac() throws NetException, SigarException {
        try {
            // 通过InetAddress的方式
            return getLocalMacByInetAddress();
        } catch (Exception e) {
            // 通过Sigar的方式
            return getLocalMacBySigar();
        }
    }

    /**
     * <p>
     * 获取本机MAC地址：通过InetAddress的方式。
     * </p>
     *
     * @return MAC地址
     * @throws NetException 获取网络信息异常：获取本机MAC地址异常！
     * @author 皮锋
     * @custom.date 2020/3/12 11:20
     */
    private static String getLocalMacByInetAddress() throws NetException {
        try {
            InetAddress ia = InetAddress.getLocalHost();
            byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                if (i != 0) {
                    sb.append(":");
                }
                // 字节转换为整数
                int temp = mac[i] & 0xff;
                String str = Integer.toHexString(temp);
                if (str.length() == 1) {
                    sb.append("0").append(str);
                } else {
                    sb.append(str);
                }
            }
            return sb.toString().toUpperCase();
        } catch (Exception e) {
            String exp = "获取本机MAC地址异常！";
            // log.error(exp, e);
            throw new NetException(exp);
        }
    }

    /**
     * <p>
     * 获取本机MAC地址：通过Sigar的方式。
     * </p>
     *
     * @return MAC地址
     * @throws NetException   获取网络信息异常：获取本机MAC地址异常！
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020/8/30 16:41
     * @since v0.0.2
     */
    private static String getLocalMacBySigar() throws SigarException, NetException {
        // 获取本机IP地址
        String ip = getLocalIp();
        NetDomain netDomain = NetInterfaceUtils.getNetInfo();
        List<NetDomain.NetInterfaceDomain> netInterfaceDomains = netDomain.getNetList();
        for (NetDomain.NetInterfaceDomain netInterfaceDomain : netInterfaceDomains) {
            // 网卡IP地址
            String address = netInterfaceDomain.getAddress();
            // 是当前网卡的IP地址
            if (StringUtils.equals(ip, address)) {
                // 返回此网卡的MAC地址
                return netInterfaceDomain.getHwAddr();
            }
        }
        // 手动抛出异常
        String exp = "获取本机MAC地址异常！";
        NetException netException = new NetException(exp);
        log.error(exp, netException);
        throw new NetException(exp);
    }

    /**
     * <p>
     * 获取本机IP地址
     * </p>
     *
     * @return IP地址
     * @throws NetException 获取网络信息异常：获取本机IP地址异常！
     * @author 皮锋
     * @custom.date 2020/3/15 18:03
     */
    public static String getLocalIp() throws NetException {
        try {
            // Windows操作系统
            if (OsUtils.isWindowsOs()) {
                return getWindowsLocalIp();
            } else {
                return getLinuxLocalIp();
            }
        } catch (Exception e) {
            String exp = "获取本机IP地址异常！";
            log.error(exp, e);
            throw new NetException(exp);
        }
    }

    /**
     * <p>
     * 获取Windows系统下的IP地址
     * </p>
     *
     * @return IP地址
     * @throws UnknownHostException 无法确定主机的IP地址异常
     * @author 皮锋
     * @custom.date 2020/12/15 10:44
     */
    private static String getWindowsLocalIp() throws UnknownHostException {
        InetAddress ip4 = InetAddress.getLocalHost();
        return ip4.getHostAddress();
    }

    /**
     * <p>
     * 获取Linux系统下的IP地址
     * </p>
     *
     * @return IP地址
     * @throws SocketException 创建或访问套接字时异常
     * @author 皮锋
     * @custom.date 2020年3月20日 上午10:19:10
     */
    private static String getLinuxLocalIp() throws SocketException {
        String ip = null;
        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
            NetworkInterface intf = en.nextElement();
            String name = intf.getName();
            if (!name.contains("docker") && !name.contains("lo")) {
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ipaddress = inetAddress.getHostAddress();
                        if (!ipaddress.contains("::") && !ipaddress.contains("0:0:")
                                && !ipaddress.contains("fe80")) {
                            ip = ipaddress;
                        }
                    }
                }
            }
        }
        return ip;
    }

    /**
     * <p>
     * 检测IP地址是否能ping通
     * </p>
     *
     * @param ip ip地址
     * @return 返回是否ping通
     * @author 皮锋
     * @custom.date 2020/12/15 10:58
     */
    public static boolean isConnect(String ip) {
        return NetUtil.ping(ip, 3000) && ping(ip);
    }

    /**
     * <p>
     * 检测IP地址是否能ping通
     * </p>
     *
     * @param ip IP地址
     * @return 返回是否ping通
     * @author 皮锋
     * @custom.date 2020/3/18 22:22
     */
    private static boolean ping(String ip) {
        boolean result;
        ProcessBuilder processBuilder = new ProcessBuilder();
        // Windows系统
        if (OsUtils.isWindowsOs()) {
            processBuilder.command("ping", ip, "-n", "5");
        } else {
            processBuilder.command("ping", ip, "-c", "5");
        }
        try {
            // 将错误信息输出流合并到标准输出流
            processBuilder.redirectErrorStream(true);
            // 执行
            Process process = processBuilder.start();
            @Cleanup
            BufferedReader buf = new BufferedReader(new InputStreamReader(process.getInputStream(), Charset.forName("GBK")));
            // 返回值
            String line;
            // ping正常的次数
            int connectedCount = 0;
            while ((line = buf.readLine()) != null) {
                log.info(line);
                connectedCount += getCheckResult(line);
            }
            // 有收到数据包
            result = connectedCount >= 1;
            // 等待命令子线程执行完成
            process.waitFor();
            // 销毁子线程
            process.destroy();
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    /**
     * <p>
     * 检测ping返回值是否是网络正常的返回值
     * </p>
     *
     * @param line ping返回的数据
     * @return 0或者1
     * @author 皮锋
     * @custom.date 2020/3/25 21:15
     */
    private static int getCheckResult(String line) {
        // 若line含有=18ms TTL=16字样，说明已经ping通，返回1，否则返回0
        //Pattern pattern = Pattern.compile("((\\d+ms)(\\s+)(TTL=\\d+))|((TTL=\\d+)(\\s+)(\\S*\\d+\\s*ms))",
        //        Pattern.CASE_INSENSITIVE);
        //Matcher matcher = pattern.matcher(line);
        //if (matcher.find()) {
        //    return 1;
        //}
        //return 0;
        if (StringUtils.containsIgnoreCase(line, "ms") && StringUtils.containsIgnoreCase(line, "ttl")) {
            return 1;
        }
        return 0;
    }

}
