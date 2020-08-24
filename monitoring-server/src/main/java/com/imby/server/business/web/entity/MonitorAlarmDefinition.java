package com.imby.server.business.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 告警定义表
 * </p>
 *
 * @author 皮锋
 * @since 2020-08-06
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "MonitorAlarmDefinition对象", description = "告警定义表")
public class MonitorAlarmDefinition implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "告警类型（SERVER、NET、INSTANCE、CUSTOM）")
    @TableField("TYPE")
    private String type;

    @ApiModelProperty(value = "一级分类")
    @TableField("FIRST_CLASS")
    private String firstClass;

    @ApiModelProperty(value = "二级分类")
    @TableField("SECOND_CLASS")
    private String secondClass;

    @ApiModelProperty(value = "三级分类")
    @TableField("THIRD_CLASS")
    private String thirdClass;

    @ApiModelProperty(value = "告警级别（INFO、WARN、ERROR、FATAL）")
    @TableField("GRADE")
    private String grade;

    @ApiModelProperty(value = "告警编码")
    @TableField("CODE")
    private String code;

    @ApiModelProperty(value = "告警标题")
    @TableField("TITLE")
    private String title;

    @ApiModelProperty(value = "告警内容")
    @TableField("CONTENT")
    private String content;

    @ApiModelProperty(value = "插入时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

}