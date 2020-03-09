package com.income.robot.code.service;

import com.income.robot.code.entity.PddMergeRule;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * <p>
 * 合并运单号规则表 服务类
 * </p>
 *
 * @author admin
 * @since 2020-02-29
 */
public interface IPddMergeRuleService extends IService<PddMergeRule> {

    /**
     * 获取规则
     */
    PddMergeRule getRule(BigDecimal price,String shopId);
}
