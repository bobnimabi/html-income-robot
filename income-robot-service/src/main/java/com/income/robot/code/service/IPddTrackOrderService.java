package com.income.robot.code.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.income.robot.code.entity.PddTrackOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.income.robot.service.dto.GoodsWaitDTO;
import com.income.robot.service.strategy.KongBaoParam;

/**
 * <p>
 * 拼多多运单号 服务类
 * </p>
 *
 * @author admin
 * @since 2020-02-26
 */
public interface IPddTrackOrderService extends IService<PddTrackOrder> {
    /**
     * 通过下单号和店铺id获取运单号
     * @param kongBaoParam
     * @return
     */
    String getTrackNo(GoodsWaitDTO kongBaoParam);

    /**
     * 绑定运单号和下单号
     * @param trackingNo
     * @param orderId
     * @param shopId
     */
    void saveTrackOrder(String trackingNo, String orderId, String shopId);
}
