package com.income.robot.code.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.income.robot.code.entity.PddTrackOrder;
import com.income.robot.code.mapper.PddTrackOrderMapper;
import com.income.robot.code.service.IPddTrackOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.income.robot.service.dto.GoodsWaitDTO;
import com.income.robot.service.strategy.KongBaoParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 拼多多运单号 服务实现类
 * </p>
 *
 * @author admin
 * @since 2020-02-26
 */
@Slf4j
@Service
public class PddTrackOrderServiceImpl extends ServiceImpl<PddTrackOrderMapper, PddTrackOrder> implements IPddTrackOrderService {

    public String getTrackNo(GoodsWaitDTO kongBaoParam) {
        PddTrackOrder trackOrder = getOne(new LambdaQueryWrapper<PddTrackOrder>()
                .eq(PddTrackOrder::getOrderNo, kongBaoParam.getOrderId())
                .eq(PddTrackOrder::getShopId, kongBaoParam.getShopId()));
        if (null == trackOrder) {
            return null;
        }
        log.info("通过下单号获取运单号成功：shopId:{},orderId:{},trackNo:{}", new Object[] { trackOrder.getShopId(), kongBaoParam.getOrderId(), trackOrder.getTrackNo() });
        return trackOrder.getTrackNo();
    }

    public void saveTrackOrder(String trackingNo, String orderId, String shopId) {
        PddTrackOrder trackOrder = new PddTrackOrder();
        trackOrder.setOrderNo(orderId);
        trackOrder.setShopId(shopId);
        trackOrder.setTrackNo(trackingNo);
        boolean isSave = save(trackOrder);
        if (isSave) {
            log.info("{},orderId:{},trackNo:{}", new Object[]{shopId, orderId, trackingNo});
        } else {
            log.info("{},orderId:{},trackNo:{}", new Object[]{shopId, orderId, trackingNo});
        }
    }
}
