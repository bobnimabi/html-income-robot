package com.income.robot.code.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.income.robot.code.entity.PddMergeRule;
import com.income.robot.code.entity.PddTrack;
import com.income.robot.code.mapper.PddTrackMapper;
import com.income.robot.code.service.IPddDictService;
import com.income.robot.code.service.IPddTrackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.income.robot.service.dto.GoodsWaitDTO;
import com.income.robot.service.strategy.KongBaoParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 拼多多运单号 服务实现类
 * </p>
 * @author admin
 * @since 2020-02-26
 */
@Slf4j
@Service
public class PddTrackServiceImpl extends ServiceImpl<PddTrackMapper, PddTrack> implements IPddTrackService {

    private static final String USE_NUM = "use_num";

    /**
     * 对于新获取的运单号的存储
     * @param kongBaoParam
     * @param trackingNo
     */
    @Async
    public void saveTrack(KongBaoParam kongBaoParam, String trackingNo, PddMergeRule mergeRule) {
        PddTrack track = new PddTrack();
        track.setMobileNum(kongBaoParam.getLoginName());
        track.setShopId(kongBaoParam.getShopId());
        track.setTrackNo(trackingNo);
        track.setUseNum(1);
        if (null != mergeRule) {
            track.setMergeRuleId(mergeRule.getId());
        }
        boolean isSave = save(track);
        if (isSave) {
            log.info("存储运单号成功：{}", JSON.toJSONString(track));
        } else {
            log.info("存储运单号失败：{}", JSON.toJSONString(track));
        }
    }

    /**
     * 更新运单号使用次数
     * 注意：此处不使用mysql乐观锁，因为次数并发概率很小，且偶有超标也无所谓
     * @param paddTrack
     */
    @Async
    public void updateTrankNo(PddTrack paddTrack) {
        int newUserNum = paddTrack.getUseNum() + 1;
        boolean isUpdate = update(new LambdaUpdateWrapper<PddTrack>()
                .eq(PddTrack::getId, paddTrack.getId())
                .set(PddTrack::getUseNum, newUserNum));
        if (!isUpdate) {
            log.info("更新运单号使用次数->失败,id:{},num:{}", paddTrack.getId(), newUserNum);
        } else {
            log.info("更新运单号使用次数->成功,id:{},num:{}", paddTrack.getId(), newUserNum);
        }
    }

    /**
     * 获取最新的满足规则的运单号
     * 条件：同一卖家、同一买家、同一合并规则
     * @return
     */
    public PddTrack getNewTrack(KongBaoParam kongBaoParam, PddMergeRule mergeRule) {
        PddTrack one = getOne(new LambdaQueryWrapper<PddTrack>()
                .eq(PddTrack::getShopId, kongBaoParam.getShopId())
                .eq(PddTrack::getMobileNum, kongBaoParam.getLoginName())
                .eq(PddTrack::getMergeRuleId, mergeRule.getId())
                .orderByDesc(PddTrack::getGmtCreateTime)
                .last("limit 1")
        );
        log.info("获取最新运单号记录为：{}", JSON.toJSONString(one));
        return one;
    }
}
