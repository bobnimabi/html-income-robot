package com.income.robot.server.chain;

import com.income.robot.code.entity.PddMergeRule;
import com.income.robot.code.entity.PddTrack;
import com.income.robot.service.chain.Filter;
import com.income.robot.service.chain.Invoker;
import com.income.robot.service.dto.GoodsWaitDTO;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;

/**
 * Created by mrt on 2020/2/29 15:17
 * 合并次数检查
 */
@Slf4j
public class NumberFilter extends Filter<RuleInvocation> {

    @Override
    protected boolean dofilter(Invoker invoker, RuleInvocation invocation) throws Exception {
        GoodsWaitDTO goodsWaitDTO = invocation.getData();
        PddMergeRule pddMergeRule = invocation.getRules();
        PddTrack mergeRecord = invocation.getMergeRecord();

        Integer mergeNum = pddMergeRule.getMergeNum();
        Integer useNum = mergeRecord.getUseNum();

        if (useNum.compareTo(mergeNum) >= 0) {
            log.info("合并次数规则校验->失败，规定次数：{},已使用次数：{}", mergeNum, useNum);
            return false;
        }
        log.info("合并次数规则校验->成功，规定次数：{},已使用次数：{}", mergeNum, useNum);
        return true;
    }
}
