package com.transfar.server.business.server.service.impl;

import com.transfar.common.domain.server.*;
import com.transfar.common.dto.ServerPackage;
import com.transfar.server.business.server.service.IServerService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务器信息服务层接口实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/23 15:23
 */
@Service
public class ServerServiceImpl implements IServerService {

    /**
     * <p>
     * 处理服务器信息包
     * </p>
     *
     * @param serverPackage 服务器信息包
     * @return boolean
     * @author 皮锋
     * @custom.date 2020/3/23 15:29
     */
    @Override
    public boolean dealServerPackage(ServerPackage serverPackage) {
        // 服务器信息
        ServerDomain serverDomain = serverPackage.getServerDomain();
        // 操作系统信息
        OsDomain osDomain = serverDomain.getOsDomain();
        // 内存信息
        MemoryDomain memoryDomain = serverDomain.getMemoryDomain();
        // Cpu信息
        CpuDomain cpuDomain = serverDomain.getCpuDomain();
        // 网卡信息
        NetDomain netDomain = serverDomain.getNetDomain();
        // java虚拟机信息
        JvmDomain jvmDomain = serverDomain.getJvmDomain();
        // 磁盘信息
        DiskDomain diskDomain = serverDomain.getDiskDomain();
        return false;
    }
}
