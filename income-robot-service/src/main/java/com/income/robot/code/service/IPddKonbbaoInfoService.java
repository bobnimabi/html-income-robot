package com.income.robot.code.service;

import com.income.robot.code.entity.PddKonbbaoInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 空包网账号信息 服务类
 * </p>
 *
 * @author admin
 * @since 2020-03-02
 */
public interface IPddKonbbaoInfoService extends IService<PddKonbbaoInfo> {
    /**
     * 获取空包网的账号信息
     */
    PddKonbbaoInfo getKongBaoInfo();
}
