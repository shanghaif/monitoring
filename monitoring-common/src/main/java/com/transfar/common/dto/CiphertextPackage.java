package com.transfar.common.dto;

import com.transfar.common.abs.SuperBean;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 密文数据包
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/4/29 12:42
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class CiphertextPackage extends SuperBean implements Serializable {

    /**
     * 加密后的数据
     */
    private String ciphertext;

}
