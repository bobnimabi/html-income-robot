package com.income.robot.service.process;

import com.income.robot.code.entity.PddMergeRule;
import com.income.robot.code.entity.PddTrack;
import com.income.robot.code.service.IPddMergeRuleService;
import com.income.robot.code.service.IPddTrackService;
import com.income.robot.service.chain.Filter;
import com.income.robot.service.chain.Invoker;
import com.income.robot.service.chain.impl.RuleInvocation;
import com.income.robot.service.strategy.KongBaoParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by mrt on 2020/3/2 16:30
 * 从数据库获取能合并的运单号，节约成本
 */
@Service
@Slf4j
public class TrackNoByMysql {

    @Autowired
    private List<Filter> filters;

    @Autowired
    private IPddMergeRuleService ruleService;

    @Autowired
    private IPddTrackService trackService;

    /**
     * 通过数据库获取符合规则的运单号
     * @param kongBaoParam  空包网参数
     * @return
     * @throws Exception
     */
    TrackResult getByMysql(KongBaoParam kongBaoParam) throws Exception {

        // 获取合并规则
        PddMergeRule rule = ruleService.getRule(kongBaoParam.getOrderPrice());
        if (null == rule) {
            return new TrackResult(false,"","无对应的合并规则");
        }

        // 获取该规则下最新的运单号，并对该运单号进行使用次数和有效期间的校验
        PddTrack track = trackService.getNewTrack(kongBaoParam, rule);
        if (null != track) {
            boolean isOk = filter(kongBaoParam, rule, track);
            if (isOk) {
                // 更新使用次数
                trackService.updateTrankNo(track);
                return new TrackResult(true,track.getTrackNo(),"数据库获取运单号成功");
            }
        }
        return new TrackResult(false, "", "数据库无符合规则的运单号");
    }

    /**
     * 通过过滤器校验规则
     * @param kongBaoParam 空包网参数
     * @param rule 合并规则
     * @param track 运单号
     * @return
     * @throws Exception
     */
    private boolean filter(KongBaoParam kongBaoParam, PddMergeRule rule, PddTrack track) throws Exception {
        RuleInvocation invocation = new RuleInvocation(kongBaoParam, rule, track);
        Invoker invoker = Invoker.buildInvokerChain(filters);
        return invoker.invoke(invocation);
    }
}
