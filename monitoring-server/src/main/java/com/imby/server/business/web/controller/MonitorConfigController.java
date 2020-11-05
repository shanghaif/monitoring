package com.imby.server.business.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.imby.server.business.web.entity.*;
import com.imby.server.business.web.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 监控配置
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/11/3 12:57
 */
@Controller
@RequestMapping("/monitor-config")
@Api(tags = "配置.监控配置")
public class MonitorConfigController {

    /**
     * 监控配置服务类
     */
    @Autowired
    private IMonitorConfigService monitorConfigService;

    /**
     * 监控网络配置服务类
     */
    @Autowired
    private IMonitorConfigNetService monitorConfigNetService;

    /**
     * 监控告警配置服务类
     */
    @Autowired
    private IMonitorConfigAlarmService monitorConfigAlarmService;

    /**
     * 监控邮件告警配置服务类
     */
    @Autowired
    private IMonitorConfigAlarmMailService monitorConfigAlarmMailService;

    /**
     * 监控短信告警配置服务类
     */
    @Autowired
    private IMonitorConfigAlarmSmsService monitorConfigAlarmSmsService;

    /**
     * <p>
     * 访问监控配置页
     * </p>
     *
     * @return {@link ModelAndView} 监控配置页
     * @author 皮锋
     * @custom.date 2020/11/3 13:01
     */
    @ApiOperation(value = "访问监控配置页")
    @GetMapping("/config")
    public ModelAndView config() {
        ModelAndView mv = new ModelAndView("set/config/config");
        MonitorConfig monitorConfig = this.monitorConfigService.getOne(new LambdaQueryWrapper<>());
        MonitorConfigNet monitorConfigNet = this.monitorConfigNetService.getOne(new LambdaQueryWrapper<>());
        MonitorConfigAlarm monitorConfigAlarm = this.monitorConfigAlarmService.getOne(new LambdaQueryWrapper<>());
        MonitorConfigAlarmMail monitorConfigAlarmMail = this.monitorConfigAlarmMailService.getOne(new LambdaQueryWrapper<>());
        MonitorConfigAlarmSms monitorConfigAlarmSms = this.monitorConfigAlarmSmsService.getOne(new LambdaQueryWrapper<>());
        mv.addObject(monitorConfig)
                .addObject(monitorConfigNet)
                .addObject(monitorConfigAlarm)
                .addObject(monitorConfigAlarmMail)
                .addObject(monitorConfigAlarmSms);
        return mv;
    }
}
