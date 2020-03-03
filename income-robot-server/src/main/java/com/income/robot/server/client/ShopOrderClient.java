package com.income.robot.server.client;

import com.bbin.common.dto.income.IncomeShopOrderQueryDTO;
import com.bbin.common.dto.income.SumValidDTO;
import com.bbin.common.response.ResponseResult;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "bbin-income",path = "/income/back/shopOrder")
public interface ShopOrderClient {

    @PostMapping("/sumValidOrdersByPrice")
    public ResponseResult sumValidOrdersByPrice(SumValidDTO sumValidDTO);
}