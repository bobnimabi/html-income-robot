package com.income.robot.server.server;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bbin.common.dto.income.TenantIncomeShopOrderDTO;
import com.bbin.common.dto.income.TenantIncomeShopOrderPaySuccessDTO;
import com.bbin.common.response.ResponseResult;
import com.bbin.utils.project.MyBeanUtil;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import com.income.robot.code.entity.PddDict;
import com.income.robot.code.entity.PddTrack;
import com.income.robot.code.entity.PddTrackOrder;
import com.income.robot.code.service.IPddDictService;
import com.income.robot.code.service.IPddTrackOrderService;
import com.income.robot.code.service.IPddTrackService;
import com.income.robot.service.mq.MqSenter;
import com.income.robot.service.dto.GoodsWaitDTO;
import com.income.robot.service.process.IProcess;
import com.income.robot.service.process.TrackResult;
import com.income.robot.service.strategy.KongBaoParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Service
public class Server {
    @Autowired
    private MqSenter mqSenter;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${fileSystem.filePath}")
    private String filePath;

    @Autowired
    private IPddTrackOrderService orderService;

    @Autowired
    private IProcess process;

    /**
     * 下单
     *
     * @param shopOrderDTO
     * @return
     * @throws Exception
     */
    public ResponseResult goodsOrder(TenantIncomeShopOrderDTO shopOrderDTO) throws Exception {
        Boolean aBoolean = this.redisTemplate.opsForValue().setIfAbsent("TRANSFER:ORDER_ID_ORDER:" + shopOrderDTO.getShopId() + ":" + shopOrderDTO.getOrderId(), "", Duration.ofMinutes(5L));
        if (!aBoolean.booleanValue()) {
            log.info("下单：重复调用，不调用mq：shopId:{},orderId:{}", shopOrderDTO.getShopId(), shopOrderDTO.getOrderId());
            return ResponseResult.SUCCESS();
        }
        String relativePath = getFilePath(shopOrderDTO);
        shopOrderDTO.setPayImageUrl("/files" + relativePath);
        this.mqSenter.sendMessage("income.orderExchange", "incomeOrderRouteKey", shopOrderDTO);
        return ResponseResult.SUCCESS();
    }

    private String getFilePath(TenantIncomeShopOrderDTO shopOrderDTO) {
        return "test";
    }

    /**
     * 待发货
     *
     * @param goodsWaitDTO
     * @return
     * @throws Exception
     */
    public ResponseResult goodsWait(GoodsWaitDTO goodsWaitDTO) throws Exception {
        String trackNo = orderService.getTrackNo(goodsWaitDTO);
        if (!StringUtils.isEmpty(trackNo)) {
            log.info("待发货：重复调用，不调用mq：shopId：{},orderId:{}，trackNo:{}", goodsWaitDTO.getShopId(), goodsWaitDTO.getOrderId(), trackNo);
            return ResponseResult.SUCCESS(trackNo);
        }

        KongBaoParam kongBaoParam = new KongBaoParam();
        MyBeanUtil.copyProperties(goodsWaitDTO, kongBaoParam);
        TrackResult trackResult = process.getTrackNo(kongBaoParam);
        if (!trackResult.getSuccess()) {
            log.info("获取运单号失败，失败原因{}，shopId:{},orderId:{},", trackResult.getError(), goodsWaitDTO.getShopId(), goodsWaitDTO.getOrderId());
            return ResponseResult.FAIL(trackResult.getError());
        }
        TenantIncomeShopOrderPaySuccessDTO paySuccessDTO = (TenantIncomeShopOrderPaySuccessDTO) MyBeanUtil.copyProperties(goodsWaitDTO, TenantIncomeShopOrderPaySuccessDTO.class);
        paySuccessDTO.setOrderType(Integer.valueOf(1));
        this.mqSenter.sendMessage("income.orderPaySuccessExchange", "incomeOrderPaySuccessRouteKey", paySuccessDTO);
        return ResponseResult.SUCCESS(trackResult.getTrackNo());
    }

    /**
     * 发货完成
     *
     * @param shopOrderDTO
     * @return
     * @throws Exception
     */
    public ResponseResult goodsComplete(@RequestBody TenantIncomeShopOrderDTO shopOrderDTO) throws Exception {
        TenantIncomeShopOrderPaySuccessDTO tenantIncomeShopOrderPaySuccessDTO = (TenantIncomeShopOrderPaySuccessDTO) MyBeanUtil.copyProperties(shopOrderDTO, TenantIncomeShopOrderPaySuccessDTO.class);
        tenantIncomeShopOrderPaySuccessDTO.setOrderType(1);
        this.mqSenter.sendMessage("income.OrderDeliverGoodsExchange", "incomeOrderDeliverGoodsRouteKey", tenantIncomeShopOrderPaySuccessDTO);
        return ResponseResult.SUCCESS();
    }

    /**
     * 确认收货
     *
     * @param shopOrderDTO
     * @return
     * @throws Exception
     */
    public ResponseResult goodsConfirm(@RequestBody TenantIncomeShopOrderDTO shopOrderDTO) throws Exception {
        TenantIncomeShopOrderPaySuccessDTO tenantIncomeShopOrderPaySuccessDTO = (TenantIncomeShopOrderPaySuccessDTO) MyBeanUtil.copyProperties(shopOrderDTO, TenantIncomeShopOrderPaySuccessDTO.class);
        tenantIncomeShopOrderPaySuccessDTO.setOrderType(1);
        this.mqSenter.sendMessage("income.orderConfirmReceiptExchange", "incomeOrderConfirmReceiptRouteKey", tenantIncomeShopOrderPaySuccessDTO);
        return ResponseResult.SUCCESS();
    }
}
