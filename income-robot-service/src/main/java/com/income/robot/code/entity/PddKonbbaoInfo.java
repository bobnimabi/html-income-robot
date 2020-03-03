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
 * 空包网账号信息
 * </p>
 *
 * @author admin
 * @since 2020-03-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PddKonbbaoInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 包网平台的id
     */
    private Long channelId;

    /**
     * 空包网名称
     */
    private String name;

    /**
     * 空包网账号
     */
    private String account;

    /**
     * 空包网密码
     */
    private String password;

    /**
     * 获取运单号的URL
     */
    private String apiUrl;

    /**
     * 空包网策略（注意：首字母小写），oneHundredStrategy
     */
    private String strategy;

    /**
     * 登录URL
     */
    private String loginUrl;

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
