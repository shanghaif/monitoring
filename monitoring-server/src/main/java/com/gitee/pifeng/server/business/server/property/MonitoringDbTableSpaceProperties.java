package com.gitee.pifeng.server.business.server.property;

import lombok.*;

/**
 * <p>
 * 数据库表空间配置属性
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/1/7 14:49
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MonitoringDbTableSpaceProperties {

    /**
     * 过载阈值
     */
    private double overloadThreshold;

    /**
     * 监控级别，四级：INFO &#60; WARN &#60; ERROR &#60; FATAL
     */
    private String level;

}
