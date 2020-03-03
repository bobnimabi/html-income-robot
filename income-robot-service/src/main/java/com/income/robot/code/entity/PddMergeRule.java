package com.income.robot.code.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 合并运单号规则表
 * </p>
 *
 * @author admin
 * @since 2020-03-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PddMergeRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 包网平台的id （机器人用于）tenant_channel表的id
     */
    private Long channelId;

    /**
     * 店铺ID
     */
    private String shopId;

    /**
     * 起始时间
     */
    private LocalTime startTime;

    /**
     * 结束时间
     */
    private LocalTime endTime;

    /**
     * 最小金额，单位：元
     */
    private BigDecimal minPrice;

    /**
     * 最大金额，单位：元
     */
    private BigDecimal maxPrice;

    /**
     * 合并次数
     */
    private Integer mergeNum;

    /**
     * 运单号有效时长，单位：秒
     */
    private Integer validTime;

    /**
     * 是否有效 1有效  0无效
     */
    private Boolean isValid;

    /**
     * 更新时间
     */
    private LocalDateTime gmtModifiedTime;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreateTime;


}
