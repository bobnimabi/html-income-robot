package com.income.robot.code.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.income.robot.code.entity.PddMergeRule;
import com.income.robot.code.mapper.PddMergeRuleMapper;
import com.income.robot.code.service.IPddMergeRuleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

/**
 * <p>
 * 合并运单号规则表 服务实现类
 * </p>
 *
 * @author admin
 * @since 2020-02-29
 */
@Slf4j
@Service
public class PddMergeRuleServiceImpl extends ServiceImpl<PddMergeRuleMapper, PddMergeRule> implements IPddMergeRuleService {

    /**
     * 获取规则
     */
    public PddMergeRule getRule(BigDecimal price) {
        LocalTime now = LocalTime.now();
        List<PddMergeRule> list = list(new LambdaQueryWrapper<PddMergeRule>()
                .gt(PddMergeRule::getMaxPrice, price)
                .le(PddMergeRule::getMinPrice, price)
                .gt(PddMergeRule::getEndTime, now)
                .le(PddMergeRule::getStartTime, now));
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        if (list.size() > 1) {
            // 仅做提示，不报错
            log.error("规则有交集，请注意，price:{},time:{}", price, LocalTime.now());
        }
        return list.get(0);
    }
}
