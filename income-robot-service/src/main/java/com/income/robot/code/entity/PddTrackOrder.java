package com.income.robot.code.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 拼多多运单号
 * </p>
 *
 * @author admin
 * @since 2020-03-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PddTrackOrder implements Serializable {

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
     * 下单号
     */
    private String orderNo;

    /**
     * 店铺id
     */
    private String shopId;

    /**
     * 运单号
     */
    private String trackNo;

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
