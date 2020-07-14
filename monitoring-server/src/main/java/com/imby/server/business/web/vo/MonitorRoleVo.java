package com.imby.server.business.web.vo;

import com.google.common.base.Converter;
import com.imby.server.business.web.entity.MonitorRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

/**
 * <p>
 * 监控用户角色表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/7/14 9:12
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "监控用户角色表现层对象")
public class MonitorRoleVo {

    @ApiModelProperty(value = "角色ID")
    private Long id;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    /**
     * <p>
     * MonitorRoleVo转MonitorRole
     * </p>
     *
     * @return {@link MonitorRole}
     * @author 皮锋
     * @custom.date 2020/7/14 10:31
     */
    public MonitorRole convertToMonitorRole() {
        MonitorRoleVoConvert monitorRoleVoConvert = new MonitorRoleVoConvert();
        return monitorRoleVoConvert.convert(this);
    }

    /**
     * <p>
     * MonitorRole转MonitorRoleVo
     * </p>
     *
     * @param monitorRole {@link MonitorRole}
     * @return {@link MonitorRoleVo}
     * @author 皮锋
     * @custom.date 2020/7/14 10:34
     */
    public MonitorRoleVo convertFor(MonitorRole monitorRole) {
        MonitorRoleVoConvert monitorRoleVoConvert = new MonitorRoleVoConvert();
        return monitorRoleVoConvert.reverse().convert(monitorRole);
    }


    /**
     * <p>
     * MonitorRoleVo转换器
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/7/14 10:25
     */
    private static class MonitorRoleVoConvert extends Converter<MonitorRoleVo, MonitorRole> {

        @Override
        protected MonitorRole doForward(MonitorRoleVo monitorRoleVo) {
            MonitorRole monitorRole = MonitorRole.builder().build();
            BeanUtils.copyProperties(monitorRoleVo, monitorRole);
            return monitorRole;
        }

        @Override
        protected MonitorRoleVo doBackward(MonitorRole monitorRole) {
            MonitorRoleVo monitorRoleVo = MonitorRoleVo.builder().build();
            BeanUtils.copyProperties(monitorRole, monitorRoleVo);
            return monitorRoleVo;
        }
    }
}
