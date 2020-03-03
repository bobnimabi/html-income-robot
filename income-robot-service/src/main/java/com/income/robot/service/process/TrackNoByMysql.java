package com.income.robot.service.process;

import com.income.robot.code.entity.PddMergeRule;
import com.income.robot.code.service.IPddMergeRuleService;
import com.income.robot.code.service.IPddTrackService;
import com.income.robot.code.service.impl.PddMergeRuleServiceImpl;
import com.income.robot.service.chain.Filter;
import com.income.robot.service.strategy.KongBaoParam;
import com.netflix.discovery.converters.Auto;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by mrt on 2020/3/2 16:30
 * 从数据库获取能合并的运单号，节约成本
 */
@Service
@Slf4j
public class TrackNoByMysql {
    @Autowired
    private Filter filters;
    @Autowired
    private IPddMergeRuleService ruleService;
    @Autowired
    private IPddTrackService trackService;

    TrackResult getByMysql(KongBaoParam kongBaoParam) {
        // 获取合并规则
        PddMergeRule rule = ruleService.getRule(kongBaoParam.getOrderPrice());
        if (null == rule) {
            return new TrackResult(false,"","无对应的合并运单号规则");
        }

        // 获取该规则下最新的运单号，并对该运单号进行使用次数和有效期间的校验










    }
}
