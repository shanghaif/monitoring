package com.imby.server.business.web.controller;


import com.imby.server.business.web.service.IMonitorJvmMemoryService;
import com.imby.server.business.web.vo.InstanceDetailPageJvmMemoryChartVo;
import com.imby.server.business.web.vo.LayUiAdminResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * java虚拟机内存信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/6 19:59
 */
@Api(tags = "应用程序.java虚拟机内存信息")
@RestController
@RequestMapping("/monitor-jvm-memory")
public class MonitorJvmMemoryController {

    /**
     * java虚拟机内存信息服务类
     */
    @Autowired
    private IMonitorJvmMemoryService monitorJvmMemoryService;

    /**
     * <p>
     * 获取应用实例详情页面java虚拟机内存图表信息
     * </p>
     *
     * @param instanceId 应用实例ID
     * @param memoryType 内存类型
     * @param time       时间
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/10/14 11:52
     */
    @ApiOperation(value = "获取应用实例详情页面java虚拟机内存图表信息")
    @ResponseBody
    @GetMapping("/get-instance-detail-page-jvm-memory-chart-info")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "instanceId", value = "应用实例ID", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "memoryType", value = "内存类型", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "time", value = "时间", paramType = "query", dataType = "string")})
    public LayUiAdminResultVo getInstanceDetailPageJvmMemoryChartInfo(@RequestParam(name = "instanceId") String instanceId,
                                                                      @RequestParam(name = "memoryType", required = false) String memoryType,
                                                                      @RequestParam(name = "time", required = false) String time) {
        List<InstanceDetailPageJvmMemoryChartVo> monitorJvmMemoryChartVos = this.monitorJvmMemoryService.getInstanceDetailPageJvmMemoryChartInfo(instanceId, memoryType, time);
        return LayUiAdminResultVo.ok(monitorJvmMemoryChartVos);
    }

}

