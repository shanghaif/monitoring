package com.gitee.pifeng.server.business.server.aop;

import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSON;
import com.gitee.pifeng.common.domain.Server;
import com.gitee.pifeng.common.domain.server.CpuDomain;
import com.gitee.pifeng.common.domain.server.DiskDomain;
import com.gitee.pifeng.common.domain.server.MemoryDomain;
import com.gitee.pifeng.common.dto.ServerPackage;
import com.gitee.pifeng.common.exception.NetException;
import com.gitee.pifeng.common.threadpool.ThreadPool;
import com.gitee.pifeng.server.business.server.controller.ServerController;
import com.gitee.pifeng.server.business.server.domain.Cpu;
import com.gitee.pifeng.server.business.server.domain.Disk;
import com.gitee.pifeng.server.business.server.domain.Memory;
import com.gitee.pifeng.server.business.server.pool.CpuPool;
import com.gitee.pifeng.server.business.server.pool.DiskPool;
import com.gitee.pifeng.server.business.server.pool.MemoryPool;
import com.gitee.pifeng.server.inf.IServerMonitoringListener;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.hyperic.sigar.SigarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 处理服务器信息的切面。
 * </p>
 * 1.把所有服务器信息添加或更新到Spring容器中的服务器信息池；<br>
 * 2.在所有服务器信息添加或更新到Spring容器中的服务器信息池之后，调用服务器信息监听器回调接口。<br>
 *
 * @author 皮锋
 * @custom.date 2020年4月1日 下午3:21:19
 */
@Slf4j
@Aspect
@Component
public class ServerAspect {

    /**
     * 服务器内存信息池
     */
    @Autowired
    private MemoryPool memoryPool;

    /**
     * 服务器CPU信息池
     */
    @Autowired
    private CpuPool cpuPool;

    /**
     * 服务器磁盘信息池
     */
    @Autowired
    private DiskPool diskPool;

    /**
     * 服务器信息监听器
     */
    @Autowired
    private List<IServerMonitoringListener> serverMonitoringListeners;

    /**
     * <p>
     * 定义切入点，切入点为{@link ServerController#acceptServerPackage(String)}这一个方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/2/22 17:56
     */
    @Pointcut("execution(public * com.gitee.pifeng.server.business.server.controller.ServerController.acceptServerPackage(..))")
    public void tangentPoint() {
    }

    /**
     * <p>
     * 通过前置通知，刷新Spring容器中的服务器信息池，并调用服务器信息监听器回调接口。
     * </p>
     *
     * @param joinPoint 提供对连接点上可用状态和有关状态的静态信息的反射访问。
     * @author 皮锋
     * @custom.date 2020年4月1日 下午3:34:06
     */
    @Before("tangentPoint()")
    public void refreshAndWakeUp(JoinPoint joinPoint) {
        String args = String.valueOf(joinPoint.getArgs()[0]);
        ServerPackage serverPackage = JSON.parseObject(args, ServerPackage.class);
        // IP地址
        String ip = serverPackage.getIp();
        // 计算机名
        String computerName = serverPackage.getComputerName();
        // 服务器信息
        Server server = serverPackage.getServer();
        // 内存信息
        MemoryDomain memoryDomain = server.getMemoryDomain();
        // Cpu信息
        CpuDomain cpuDomain = server.getCpuDomain();
        // 磁盘信息
        DiskDomain diskDomain = server.getDiskDomain();
        // 刷新服务器信息
        this.refreshServerInfo(ip, computerName, memoryDomain, cpuDomain, diskDomain);
        // 调用监听器回调接口
        this.serverMonitoringListeners.forEach(o -> ThreadPool.COMMON_IO_INTENSIVE_THREAD_POOL.execute(() -> {
            try {
                o.wakeUpMonitor(ip);
            } catch (NetException e) {
                log.error("获取网络信息异常！", e);
            } catch (SigarException e) {
                log.error("Sigar异常！", e);
            }
        }));
    }

    /**
     * <p>
     * 刷新服务器信息
     * </p>
     *
     * @param ip           IP地址
     * @param computerName 计算机名
     * @param memoryDomain 内存信息
     * @param cpuDomain    CPU信息
     * @param diskDomain   磁盘信息
     * @author 皮锋
     * @custom.date 2020/3/31 13:39
     */
    private void refreshServerInfo(String ip, String computerName, MemoryDomain memoryDomain, CpuDomain cpuDomain, DiskDomain diskDomain) {
        // 刷新服务器内存信息
        this.refreshMemory(ip, computerName, memoryDomain);
        // 刷新服务器CPU信息
        this.refreshCpu(ip, computerName, cpuDomain);
        // 刷新服务器磁盘信息
        this.refreshDisk(ip, computerName, diskDomain);
    }

    /**
     * <p>
     * 刷新服务器磁盘信息
     * </p>
     *
     * @param ip           IP地址
     * @param computerName 计算机名
     * @param diskDomain   磁盘信息
     * @author 皮锋
     * @custom.date 2020/3/31 13:44
     */
    private void refreshDisk(String ip, String computerName, DiskDomain diskDomain) {
        Disk disk = new Disk();
        Map<String, Disk.Subregion> subregionMap = new HashMap<>(16);
        for (DiskDomain.DiskInfoDomain diskInfoDomain : diskDomain.getDiskInfoList()) {
            Disk.Subregion subregion = new Disk.Subregion();
            // 盘符名字
            String devName = diskInfoDomain.getDevName();
            // 盘符使用率
            double usePercent = NumberUtil.round(diskInfoDomain.getUsePercent() * 100D, 2).doubleValue();
            subregion.setUsePercent(usePercent);
            subregion.setDevName(devName);
            Disk.Subregion poolDiskSubregion = this.diskPool.get(ip) != null ? this.diskPool.get(ip).getSubregionMap().get(devName) : null;
            subregion.setAlarm(poolDiskSubregion != null && poolDiskSubregion.isAlarm());
            subregion.setOverLoad(poolDiskSubregion != null && poolDiskSubregion.isOverLoad());
            subregionMap.put(devName, subregion);
        }
        disk.setIp(ip);
        disk.setComputerName(computerName);
        disk.setSubregionMap(subregionMap);
        this.diskPool.updateDiskPool(ip, disk);
    }

    /**
     * <p>
     * 刷新服务器CPU信息
     * </p>
     *
     * @param ip           IP地址
     * @param computerName 计算机名
     * @param cpuDomain    CPU信息
     * @author 皮锋
     * @custom.date 2020/3/31 13:43
     */
    private void refreshCpu(String ip, String computerName, CpuDomain cpuDomain) {
        Cpu cpu = new Cpu();
        cpu.setIp(ip);
        cpu.setComputerName(computerName);
        cpu.setCpuDomain(cpuDomain);
        cpu.setAvgCpuCombined(Cpu.calculateAvgCpuCombined(cpuDomain));
        Cpu poolCpu = this.cpuPool.get(ip);
        cpu.setNum(poolCpu != null ? poolCpu.getNum() : 0);
        cpu.setAlarm(poolCpu != null && poolCpu.isAlarm());
        cpu.setOverLoad(poolCpu != null && poolCpu.isOverLoad());
        // 更新服务器CPU信息池
        this.cpuPool.updateCpuPool(ip, cpu);
    }

    /**
     * <p>
     * 刷新服务器内存信息
     * </p>
     *
     * @param ip           IP地址
     * @param computerName 计算机名
     * @param memoryDomain 内存信息
     * @author 皮锋
     * @custom.date 2020/3/31 13:41
     */
    private void refreshMemory(String ip, String computerName, MemoryDomain memoryDomain) {
        Memory memory = new Memory();
        memory.setIp(ip);
        memory.setComputerName(computerName);
        memory.setMemoryDomain(memoryDomain);
        memory.setUsedPercent(NumberUtil.round(memoryDomain.getMenDomain().getMenUsedPercent() * 100D, 2).doubleValue());
        Memory poolMemory = this.memoryPool.get(ip);
        memory.setNum(poolMemory != null ? poolMemory.getNum() : 0);
        memory.setAlarm(poolMemory != null && poolMemory.isAlarm());
        memory.setOverLoad(poolMemory != null && poolMemory.isOverLoad());
        // 更新服务器内存信息池
        this.memoryPool.updateMemoryPool(ip, memory);
    }

}