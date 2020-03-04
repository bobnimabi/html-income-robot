package com.income.robot.service.chain.impl;

import com.income.robot.code.entity.PddMergeRule;
import com.income.robot.code.entity.PddTrack;
import com.income.robot.service.chain.Invocation;
import com.income.robot.service.strategy.KongBaoParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
public class RuleInvocation implements Invocation {

    /**
     * 请求数据
      */
    private KongBaoParam data;

    /**
     * 规则
     */
    private PddMergeRule rule;

    /**
     * 符合规则的合并记录
     */
    private PddTrack mergeRecord;


    public RuleInvocation() {}

    public RuleInvocation(KongBaoParam data, PddMergeRule rules, PddTrack mergeRecord) {
        this.data = data;
        this.rule = rules;
        this.mergeRecord = mergeRecord;
    }
}
