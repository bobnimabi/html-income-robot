package com.income.robot.code.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.income.robot.code.entity.PddMergeRule;
import com.income.robot.code.mapper.PddMergeRuleMapper;
import com.income.robot.code.service.IPddMergeRuleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
    // 全局shopId默认值
    private static final String SHOP_ID_GLOBE = "0";

    /**
     * 获取规则
     */
    public PddMergeRule getRule(BigDecimal price, String shopId) {
        if (StringUtils.isEmpty(shopId)) {
            return getRule(price);
        } else {
            PddMergeRule rule = getRuleByShopId(price, shopId);
            return null == rule ? getRule(price) : rule;
        }
    }

    /**
     * 获取合并规则（价格、时间）
     * @param price
     * @return
     */
    private PddMergeRule getRule(BigDecimal price) {
        List<PddMergeRule> list = getByCriteria(price, SHOP_ID_GLOBE);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        if (list.size() > 1) {
            log.error("合并规则有交集，请注意，price:{},time:{}", price, LocalTime.now());
        }
        return list.get(0);
    }

    /**
     * 获取合并规则（价格、时间、店铺ID）
     * @param price
     * @param shopId
     * @return
     */
    private PddMergeRule getRuleByShopId(BigDecimal price, String shopId) {
        Assert.hasText(shopId,"shopId没有值");
        List<PddMergeRule> list = getByCriteria(price, shopId);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        if (list.size() > 1) {
            log.error("shopId合并规则有交集，请注意，price:{},time:{},shopId:{}", price, LocalTime.now(), shopId);
        }
        return list.get(0);
    }

    private List<PddMergeRule> getByCriteria(BigDecimal price, String shopId) {
        LocalTime now = LocalTime.now();
        LambdaQueryWrapper<PddMergeRule> wrapper = new LambdaQueryWrapper<PddMergeRule>()
                .gt(PddMergeRule::getMaxPrice, price)
                .le(PddMergeRule::getMinPrice, price)
                .gt(PddMergeRule::getEndTime, now)
                .le(PddMergeRule::getStartTime, now);

        if (!StringUtils.isEmpty(shopId)) {
            wrapper.eq(PddMergeRule::getShopId, shopId);
        }

        return list(wrapper);
    }
}
