package com.income.robot.service.chain.impl;

import com.income.robot.code.entity.PddMergeRule;
import com.income.robot.code.entity.PddTrack;
import com.income.robot.service.chain.Filter;
import com.income.robot.service.chain.Invoker;
import com.income.robot.service.strategy.KongBaoParam;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * Created by mrt on 2020/2/29 15:17
 * 合并次数检查
 */
@Slf4j
public class ValidTimeFilter extends Filter<RuleInvocation> {

    @Override
    protected boolean dofilter(Invoker invoker, RuleInvocation invocation) throws Exception {
        KongBaoParam goodsWaitDTO = invocation.getData();
        PddMergeRule pddMergeRule = invocation.getRule();
        PddTrack mergeRecord = invocation.getMergeRecord();

        Integer validTime = pddMergeRule.getValidTime();
        LocalDateTime maxTime = mergeRecord.getGmtCreateTime().plusSeconds(validTime);
        LocalDateTime now = LocalDateTime.now();

        if (now.compareTo(maxTime) > 0) {
            log.info("有效时间规则校验->失败，创建时间：{},有效时间(秒)：{}", mergeRecord.getGmtCreateTime(), validTime);
            return false;
        }
        log.info("有效时间规则校验->成功，创建时间：{},有效时间(秒)：{}", mergeRecord.getGmtCreateTime(), validTime);
        return true;
    }
}
