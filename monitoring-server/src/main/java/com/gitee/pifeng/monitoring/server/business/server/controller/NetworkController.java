package com.gitee.pifeng.monitoring.server.business.server.controller;

import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.dto.BaseResponsePackage;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.server.business.server.core.PackageConstructor;
import com.gitee.pifeng.monitoring.server.business.server.service.INetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 网络信息控制器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/10/6 21:56
 */
@RestController
@RequestMapping("/network")
@Api(tags = "网络信息")
public class NetworkController {

    /**
     * 网络信息服务接口
     */
    @Autowired
    private INetService netService;

    /**
     * <p>
     * 获取被监控网络源IP地址
     * </p>
     *
     * @return {@link BaseResponsePackage}
     * @throws NetException 自定义获取网络信息异常
     * @author 皮锋
     * @custom.date 2021/10/6 22:04
     */
    @ApiOperation(value = "获取被监控网络源IP地址", notes = "获取被监控网络源IP地址")
    @PostMapping("/get-source-ip")
    public BaseResponsePackage getSourceIp() throws NetException {
        String ipSource = this.netService.getSourceIp();
        return new PackageConstructor().structureBaseResponsePackage(Result.builder().isSuccess(true).msg(ipSource).build());
    }

}
