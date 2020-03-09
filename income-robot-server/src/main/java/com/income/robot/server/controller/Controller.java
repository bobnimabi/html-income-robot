package com.income.robot.server.controller;


import com.alibaba.fastjson.JSON;
import com.bbin.common.dto.income.SumValidDTO;
import com.bbin.common.dto.income.TenantIncomeShopOrderDTO;
import com.bbin.common.response.ResponseResult;
import com.income.robot.server.client.ShopOrderClient;
import com.income.robot.service.common.Constant;
import com.income.robot.service.dto.GoodsWaitDTO;
import com.income.robot.server.server.Server;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
public class Controller {
    @Autowired
    private Server server;

    @Autowired
    private ShopOrderClient shopOrderClient;

    @PostMapping({"/goodsOrder"})
    public ResponseResult goodsOrder(@RequestBody TenantIncomeShopOrderDTO shopOrderDTO) throws Exception {
        if (null == shopOrderDTO)
            return ResponseResult.FAIL("shopOrderDTO不存在");
        log.info("下单：接口参数：{}", JSON.toJSONString(shopOrderDTO));
        if (StringUtils.isEmpty(shopOrderDTO.getShopId()))
            return ResponseResult.FAIL("shopId为空");
        if (StringUtils.isEmpty(shopOrderDTO.getOrderId()))
            return ResponseResult.FAIL("orderId为空");
        if (!Constant.PAY_TYPE_WECHAT.equals(shopOrderDTO.getPayChannel()) && !Constant.PAY_TYPE_ALIPAY.equals(shopOrderDTO.getPayChannel()))
            return ResponseResult.FAIL("payChannel为空");
        if (StringUtils.isEmpty(shopOrderDTO.getLoginName()))
            return ResponseResult.FAIL("loginName为空");
        if (Constant.PAY_TYPE_WECHAT.equals(shopOrderDTO.getPayChannel()) && StringUtils.isEmpty(shopOrderDTO.getPayUrl()))
            return ResponseResult.FAIL("微信：payUrl不存在");
        if (Constant.PAY_TYPE_ALIPAY.equals(shopOrderDTO.getPayChannel()) &&
                StringUtils.isEmpty(shopOrderDTO.getPayUrl()))
            return ResponseResult.FAIL("支付宝：payUrl不存在");
        if (shopOrderDTO.getPayUrl().contains("undefined"))
            return ResponseResult.SUCCESS("payurl包含undefined");
        if (null == shopOrderDTO.getOrderType())
            return ResponseResult.FAIL("orderType为空");
        return this.server.goodsOrder(shopOrderDTO);
    }

    @PostMapping({"/goodsWait"})
    public ResponseResult goodsWait(@RequestBody GoodsWaitDTO goodsWaitDTO) throws Exception {
        if (null == goodsWaitDTO)
            return ResponseResult.FAIL("goodsWaitDTO为空");
        log.info("待发货：接口参数：{}", JSON.toJSONString(goodsWaitDTO));
        if (StringUtils.isEmpty(goodsWaitDTO.getShopId()))
            return ResponseResult.FAIL("shopId为空");
        if (StringUtils.isEmpty(goodsWaitDTO.getOrderId()))
            return ResponseResult.FAIL("orderId为空");
        if (StringUtils.isEmpty(goodsWaitDTO.getLoginName()))
            return ResponseResult.FAIL("loginName为空");
        if (null == goodsWaitDTO.getOrderType())
            return ResponseResult.FAIL("orderType为空");
        return this.server.goodsWait(goodsWaitDTO);
    }

    @PostMapping({"/goodsComplete"})
    public ResponseResult goodsComplete(@RequestBody TenantIncomeShopOrderDTO shopOrderDTO) throws Exception {
        if (null == shopOrderDTO)
            return ResponseResult.FAIL("shopOrderDTO为空");
        log.info("发货完成：接口参数：{}", JSON.toJSONString(shopOrderDTO));
        if (StringUtils.isEmpty(shopOrderDTO.getShopId()))
            return ResponseResult.FAIL("shopId为空");
        if (StringUtils.isEmpty(shopOrderDTO.getOrderId()))
            return ResponseResult.FAIL("orderId为空");
        if (null == shopOrderDTO.getOrderType())
            return ResponseResult.FAIL("orderType为空");
        return this.server.goodsComplete(shopOrderDTO);
    }

    @PostMapping({"/goodsConfirm"})
    public ResponseResult goodsConfirm(@RequestBody TenantIncomeShopOrderDTO shopOrderDTO) throws Exception {
        if (null == shopOrderDTO)
            return ResponseResult.FAIL("shopOrderDTO为空");
        log.info("确认收货：接口参数：{}", JSON.toJSONString(shopOrderDTO));
        if (StringUtils.isEmpty(shopOrderDTO.getShopId()))
            return ResponseResult.FAIL("shopId为空");
        if (StringUtils.isEmpty(shopOrderDTO.getOrderId()))
            return ResponseResult.FAIL("orderId为空");
        if (null == shopOrderDTO.getOrderType())
            return ResponseResult.FAIL("orderType为空");
        return this.server.goodsConfirm(shopOrderDTO);
    }

    @Autowired
    private RestTemplate restTemplate;
    @PostMapping("/shopOrder/sumValidOrdersByPrice")
    @ApiOperation(value = "某个价格可用订单数据", httpMethod = "POST", consumes = "application/json")
    public ResponseResult sumValidOrdersByPrice(@ApiParam(name = "sumValidDTO", value = "查询条件") @RequestBody SumValidDTO sumValidDTO) {
        log.info("某个价格可用订单数据：{}", JSON.toJSONString(sumValidDTO));
        return shopOrderClient.sumValidOrdersByPrice(sumValidDTO);
    }
}
