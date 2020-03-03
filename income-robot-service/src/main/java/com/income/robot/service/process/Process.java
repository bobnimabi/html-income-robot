package com.income.robot.service.process;

import com.alibaba.fastjson.JSON;
import com.income.robot.code.entity.PddKonbbaoAccount;
import com.income.robot.code.entity.PddKonbbaoInfo;
import com.income.robot.code.service.IPddKonbbaoAccountService;
import com.income.robot.code.service.IPddKonbbaoInfoService;
import com.income.robot.service.strategy.IKongBaoStragegy;
import com.income.robot.service.strategy.KongBaoParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 获取TrackNo流程
 */
@Slf4j
public class Process implements IProcess {

    @Override
    public TrackResult getTrackNo(KongBaoParam kongBaoParam) {
        return getTrackNoByKongBao(kongBaoParam);
    }

}
