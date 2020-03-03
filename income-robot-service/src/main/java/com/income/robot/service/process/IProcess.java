package com.income.robot.service.process;


import com.income.robot.service.strategy.KongBaoParam;

/**
 * Created by mrt on 2019/8/12 0012 上午 9:44
 */
public interface IProcess {

    /**
     * 从空包网获取运单号
     * @param kongBaoParam
     * @return
     */
    TrackResult getTrackNo(KongBaoParam kongBaoParam);
}
