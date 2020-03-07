package com.transfar.util;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

import com.transfar.core.ConfigLoader;

/**
 * <p>
 * 应用实例工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月4日 下午10:41:27
 */
public class InstanceUtils {

	/**
	 * <p>
	 * 获取java进程PID
	 * </p>
	 *
	 * @author 皮锋
	 * @custom.date 2020年3月4日 下午10:48:07
	 * @return PID
	 */
	private static String getJavaPid() {
		String pid = null;
		RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
		String name = runtime.getName();
		int index = name.indexOf("@");
		if (index != -1) {
			pid = name.substring(0, index);
		}
		return pid;
	}

	/**
	 * <p>
	 * 获取应用实例ID
	 * </p>
	 *
	 * @author 皮锋
	 * @custom.date 2020年3月4日 下午11:12:46
	 * @return 应用实例ID
	 */
	public static String getInstanceId() {
		String instanceName = getInstanceName();
		String md5 = MD5Utils.getMD5String(instanceName);
		return md5 + getJavaPid();
	}

	/**
	 * <p>
	 * 获取应用实例名字
	 * </p>
	 *
	 * @author 皮锋
	 * @custom.date 2020年3月6日 下午9:16:00
	 * @return 应用实例名字
	 */
	public static String getInstanceName() {
		return ConfigLoader.monitoringProperties.getOwnProperties().getInstanceName();
	}

	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i < 100; i++) {
			String id = getInstanceId();
			System.out.println("当前应用的ID为：" + id);
			Thread.sleep(5 * 1000);
		}
	}
}