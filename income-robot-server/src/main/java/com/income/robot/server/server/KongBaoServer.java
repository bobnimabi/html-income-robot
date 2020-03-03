package com.income.robot.server.server;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.income.robot.code.entity.PddDict;
import com.income.robot.code.service.IPddDictService;
import com.income.robot.server.kongbao.KongBaoApi;
import com.income.robot.service.dto.GoodsWaitDTO;
import com.income.robot.service.dto.Raddress;
import com.income.robot.service.dto.SenderOuterDTO;
import com.income.robot.service.vo.SenderVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by mrt on 2020/2/29 19:44
 */
@Slf4j
@Service
public class KongBaoServer {
    
    @Autowired
    private KongBaoApi senderApi;

    @Autowired
    private IPddDictService dictService;

    String getTrackNoByKongBao(GoodsWaitDTO goodsWaitDTO, StringBuilder error) throws InterruptedException {
        SenderVO senderVO = null;
        int num = 3;
        for (int i = 0; i < num; i++) {
            senderVO = send(goodsWaitDTO);
            if (null == senderVO) {
                log.error("{},orderId:{},{}", new Object[] { goodsWaitDTO.getShopId(), goodsWaitDTO.getOrderId(), Integer.valueOf(num - 1 - i) });
                Thread.sleep(1000L);
            } else {
                if (!"0".equals(senderVO.getCode())) {
                    error.append(senderVO.getMsg());
                    log.error("{},orderId:{},{}", new Object[] { goodsWaitDTO.getShopId(), goodsWaitDTO.getOrderId(), JSON.toJSONString(senderVO) });
                    return null;
                }
                break;
            }
        }
        if (null == senderVO) {
            log.error("{},orderId:{}", goodsWaitDTO.getShopId(), goodsWaitDTO.getOrderId());
            return null;
        }
        return senderVO.getData()[0].getTrackingno();
    }

    /**
     * 组装发送参数
     * @param goodsWaitDTO 待发货请求参数
     * @return
     */
    private SenderVO send(GoodsWaitDTO goodsWaitDTO) {
        SenderOuterDTO senderOuterDTO = new SenderOuterDTO();
        senderOuterDTO.setPlatform(goodsWaitDTO.getPlatform());
        senderOuterDTO.setSendContact(goodsWaitDTO.getSendContact());
        senderOuterDTO.setSendState(goodsWaitDTO.getSendState());
        senderOuterDTO.setSendCity(goodsWaitDTO.getSendCity());
        senderOuterDTO.setSendDistrict(goodsWaitDTO.getSendDistrict());
        Raddress[] raddressArr = new Raddress[1];
        Raddress raddress = new Raddress();
        raddress.setOrderNo(goodsWaitDTO.getOrderId());
        raddress.setContact(goodsWaitDTO.getContact());
        raddress.setState(goodsWaitDTO.getState());
        raddress.setCity(goodsWaitDTO.getCity());
        raddress.setDistrict(goodsWaitDTO.getDistrict());
        raddress.setAddress(goodsWaitDTO.getAddress());
        raddressArr[0] = raddress;
        senderOuterDTO.setRaddress(raddressArr);
        String account = getDict("account");
        String password = getDict("password");
        String result = this.senderApi.send(account, password, "buykongbao", senderOuterDTO);
        if (StringUtils.isEmpty(result)) {
            log.error("空包网响应结果为空");
            return null;
        }
        SenderVO senderVO = (SenderVO)JSON.parseObject(result, SenderVO.class);
        log.info("{}", JSON.toJSONString(senderVO));
        return senderVO;
    }

    private String getDict(String key) {
        PddDict dict = dictService.getOne(new LambdaQueryWrapper<PddDict>()
                .eq(PddDict::getDictKey, key));
        return dict.getDictValue();
    }
}
