package com.income.robot.service.strategy.impl;

import com.alibaba.fastjson.JSON;
import com.income.robot.code.entity.PddKonbbaoInfo;
import com.income.robot.service.dto.Raddress;
import com.income.robot.service.dto.SenderOuterDTO;
import com.income.robot.service.process.TrackResult;
import com.income.robot.service.strategy.IKongBaoStragegy;
import com.income.robot.service.strategy.KongBaoParam;
import com.income.robot.service.vo.SenderVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by mrt on 2020/3/2 14:11
 * 空包100官网：http://www.kongbao100.com/login.asp
 */
@Slf4j
@Service
public class OneHundredStrategy implements IKongBaoStragegy {
    @Autowired
    private KongBaoApi kongBaoApi;

    // 空包100：接口类型
    private static final String TYPE = "buykongbao";

    // 空包100：响应成功状态
    private static final String SUCCESS = "0";

    @Override
    public TrackResult generateTrackNo(KongBaoParam kongBaoParam, PddKonbbaoInfo konbbaoAccount) {
        String result = kongBaoApi.send(konbbaoAccount, TYPE, createParams(kongBaoParam));
        log.info("空包网100：接口响应:{}", result);
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        SenderVO senderVO = (SenderVO) JSON.parseObject(result, SenderVO.class);
        return new TrackResult(
                SUCCESS.equals(senderVO.getCode()),
                null != senderVO.getData() && null != senderVO.getData()[0] ? senderVO.getData()[0].getTrackingno() : "",
                senderVO.getMsg()
        );
    }

    /**
     * 组装空包网参数
     * @param params 待发货请求参数
     * @return
     */
    private SenderOuterDTO createParams(KongBaoParam params) {
        SenderOuterDTO senderOuterDTO = new SenderOuterDTO();
        senderOuterDTO.setPlatform(params.getPlatform());
        senderOuterDTO.setSendContact(params.getSendContact());
        senderOuterDTO.setSendState(params.getSendState());
        senderOuterDTO.setSendCity(params.getSendCity());
        senderOuterDTO.setSendDistrict(params.getSendDistrict());
        Raddress[] raddressArr = new Raddress[1];
        Raddress raddress = new Raddress();
        raddress.setOrderNo(params.getOrderId());
        raddress.setContact(params.getContact());
        raddress.setState(params.getState());
        raddress.setCity(params.getCity());
        raddress.setDistrict(params.getDistrict());
        raddress.setAddress(params.getAddress());
        raddressArr[0] = raddress;
        senderOuterDTO.setRaddress(raddressArr);
        return senderOuterDTO;
    }
}
