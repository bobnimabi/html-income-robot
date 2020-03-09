package com.income.robot.service.process;

import com.income.robot.code.entity.PddKonbbaoInfo;
import com.income.robot.code.entity.PddMergeRule;
import com.income.robot.code.service.IPddKonbbaoInfoService;
import com.income.robot.code.service.IPddMergeRuleService;
import com.income.robot.code.service.IPddTrackService;
import com.income.robot.service.strategy.IKongBaoStragegy;
import com.income.robot.service.strategy.KongBaoParam;
import com.income.robot.service.strategy.impl.OneHundredStrategy;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * Created by mrt on 2020/3/2 16:30
 */
@Service
@Slf4j
public class TrackNoByKongBao {
    /**
     * 这里用到spring的自动填充的特性
     */
    @Autowired
    private Map<String, IKongBaoStragegy> stragegyMap;

    @Autowired
    private IPddKonbbaoInfoService kongBaoInfoService;

    @Autowired
    private OneHundredStrategy DEFAULT_STRATEGY;

    @Autowired
    private IPddTrackService trackService;

    @Autowired
    private IPddMergeRuleService ruleService;


    /**
     * 获取运单号（从空包网）
     * @param kongBaoParam
     * @return
     * @throws InterruptedException
     */
    TrackResult getTrackNoByKongBao(KongBaoParam kongBaoParam) throws InterruptedException {
        PddKonbbaoInfo info = kongBaoInfoService.getKongBaoInfo();
        IKongBaoStragegy stragegy = stragegyMap.getOrDefault(info.getStrategy(), DEFAULT_STRATEGY);
        Assert.notNull(stragegy,"未配置获取运单号的策略");

        TrackResult result = getTrackNoByKongBaoDetail(stragegy, info, kongBaoParam);
        if (result.getSuccess()) {
            // 数据库存入一份运单号
            PddMergeRule rule = ruleService.getRule(kongBaoParam.getOrderPrice(), kongBaoParam.getShopId());
            trackService.saveTrack(kongBaoParam, result.getTrackNo(), rule);
        }
        return result;
    }

    /**
     * 调用空包网细节:尝试3次
     * @param stragegy
     * @param info
     * @param kongBaoParam
     * @return
     * @throws InterruptedException
     */
    private TrackResult getTrackNoByKongBaoDetail(IKongBaoStragegy stragegy,PddKonbbaoInfo info,KongBaoParam kongBaoParam) throws InterruptedException {
        String shopId = kongBaoParam.getShopId();
        String orderId = kongBaoParam.getOrderId();
        final int num =3;
        TrackResult trackResult = null;
        for (int i = 0; i < num; i++) {
            trackResult = stragegy.generateTrackNo(kongBaoParam, info);
            if (null == trackResult) {
                log.error("空包网未响应：shopId:{},orderId:{},尝试剩余次数{}", shopId, orderId, Integer.valueOf(num - 1 - i));
                Thread.sleep(1000L);
                continue;
            }
            break;
        }
        return null == trackResult ? new TrackResult(false, "", "空包网未响应") : trackResult;
    }
}
