package com.transfar.common.property;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 服务器信息属性
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月8日 下午2:27:42
 */
@Data
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class MonitoringServerInfoProperties {

	/**
	 * 发送服务器信息的频率
	 */
	private long rate;

}