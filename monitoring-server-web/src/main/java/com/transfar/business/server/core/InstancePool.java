package com.transfar.business.server.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.transfar.business.server.domain.Instance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 应用实例池，维护哪些应用在线，哪些应用离线
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月9日 上午10:53:52
 */
@SuppressWarnings("serial")
@Slf4j
@Component
public class InstancePool extends ConcurrentHashMap<String, Instance> {

    /**
     * <p>
     * 对象转Json字符串
     * </p>
     *
     * @return Json
     * @author 皮锋
     * @custom.date 2020年3月4日 上午10:16:43
     */
    public String toJsonString() {
        return JSON.toJSONString(this, SerializerFeature.WriteMapNullValue);
    }

}