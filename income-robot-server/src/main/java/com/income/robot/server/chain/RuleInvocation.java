package com.income.robot.server.chain;

import com.income.robot.code.entity.PddMergeRule;
import com.income.robot.code.entity.PddTrack;
import com.income.robot.service.chain.Invocation;
import com.income.robot.service.dto.GoodsWaitDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleInvocation implements Invocation {

    /**
     * 请求数据
      */
    private GoodsWaitDTO data;

    /**
     * 规则
     */
    private PddMergeRule rules;

    /**
     * 符合规则的合并记录
     */
    private PddTrack mergeRecord;

    /**
     * filter返回false时，写明错误原因
     */
    private String error;

}
