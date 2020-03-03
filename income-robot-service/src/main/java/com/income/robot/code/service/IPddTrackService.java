package com.income.robot.code.service;

import com.income.robot.code.entity.PddMergeRule;
import com.income.robot.code.entity.PddTrack;
import com.baomidou.mybatisplus.extension.service.IService;
import com.income.robot.service.strategy.KongBaoParam;

/**
 * <p>
 * 拼多多运单号 服务类
 * </p>
 *
 * @author admin
 * @since 2020-02-26
 */
public interface IPddTrackService extends IService<PddTrack> {

    /**
     * 对于新获取的运单号的存储
     * @param kongBaoParam 空包网的下单参数
     * @param trackingNo 运单号
     * @param mergeRule 合并规则
     */
    void saveTrack(KongBaoParam kongBaoParam, String trackingNo, PddMergeRule mergeRule);


    /**
     * 更新运单号使用次数
     * 注意：此处不使用mysql乐观锁，因为次数并发概率很小，且偶有超标也无所谓
     * @param paddTrack
     */
    void updateTrankNo(PddTrack paddTrack);
}
