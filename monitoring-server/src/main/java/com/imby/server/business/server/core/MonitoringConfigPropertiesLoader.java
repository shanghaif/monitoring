package com.imby.server.business.server.core;

import cn.hutool.core.util.StrUtil;
import com.imby.server.business.server.entity.*;
import com.imby.server.business.server.property.*;
import com.imby.server.business.server.service.IMonitorConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * <p>
 * 监控配置属性加载器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/23 14:13
 */
@Slf4j
@Component
public class MonitoringConfigPropertiesLoader {

    /**
     * 监控配置服务接口
     */
    @Autowired
    private IMonitorConfigService monitorConfigService;

    /**
     * <p>
     * 获取监控配置属性
     * </p>
     *
     * @return {@link MonitoringProperties}
     * @author 皮锋
     * @custom.date 2020/11/4 13:01
     */
    @Bean
    public MonitoringProperties loadAllMonitorConfig() {
        Map<String, Object> allMonitorConfig = this.monitorConfigService.loadAllMonitorConfig();
        MonitorConfigAlarmMail monitorConfigAlarmMail = (MonitorConfigAlarmMail) allMonitorConfig.get("monitorConfigAlarmMail");
        MonitorConfigAlarmSms monitorConfigAlarmSms = (MonitorConfigAlarmSms) allMonitorConfig.get("monitorConfigAlarmSms");
        MonitorConfigAlarm monitorConfigAlarm = (MonitorConfigAlarm) allMonitorConfig.get("monitorConfigAlarm");
        MonitorConfigNet monitorConfigNet = (MonitorConfigNet) allMonitorConfig.get("monitorConfigNet");
        MonitorConfig monitorConfig = (MonitorConfig) allMonitorConfig.get("monitorConfig");
        // 告警邮箱配置属性
        MonitoringAlarmMailProperties monitoringAlarmMailProperties = new MonitoringAlarmMailProperties();
        monitoringAlarmMailProperties.setEmills(StrUtil.splitToArray(monitorConfigAlarmMail.getEmills(), ';'));
        // 告警短信配置属性
        MonitoringAlarmSmsProperties monitoringAlarmSmsProperties = new MonitoringAlarmSmsProperties();
        monitoringAlarmSmsProperties.setPhoneNumbers(StrUtil.splitToArray(monitorConfigAlarmSms.getPhoneNumbers(), ';'));
        monitoringAlarmSmsProperties.setAddress(monitorConfigAlarmSms.getAddress());
        monitoringAlarmSmsProperties.setProtocol(monitorConfigAlarmSms.getProtocol());
        monitoringAlarmSmsProperties.setEnterprise(monitorConfigAlarmSms.getEnterprise());
        // 告警配置属性
        MonitoringAlarmProperties monitoringAlarmProperties = new MonitoringAlarmProperties();
        monitoringAlarmProperties.setEnable(monitorConfigAlarm.getEnable().equals(1));
        monitoringAlarmProperties.setLevel(monitorConfigAlarm.getLevel());
        monitoringAlarmProperties.setWay(StrUtil.splitToArray(monitorConfigAlarm.getWay(), ';'));
        monitoringAlarmProperties.setSmsProperties(monitoringAlarmSmsProperties);
        monitoringAlarmProperties.setMailProperties(monitoringAlarmMailProperties);
        // 网络配置属性
        MonitoringNetworkProperties monitoringNetworkProperties = new MonitoringNetworkProperties();
        monitoringNetworkProperties.setMonitoringEnable(monitorConfigNet.getEnable().equals(1));
        // 监控配置属性
        MonitoringProperties monitoringProperties = new MonitoringProperties();
        monitoringProperties.setThreshold(monitorConfig.getThreshold());
        monitoringProperties.setAlarmProperties(monitoringAlarmProperties);
        monitoringProperties.setNetworkProperties(monitoringNetworkProperties);
        log.info("监控服务端配置加载成功！");
        return monitoringProperties;
    }

}
