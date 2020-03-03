package com.income.robot.service.process;

import com.income.robot.service.strategy.KongBaoParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 获取TrackNo流程
 */
@Slf4j
public class Process implements IProcess {

    @Autowired
    private TrackNoByKongBao trackNoByKongBao;

    @Autowired
    private TrackNoByMysql trackNoByMysql;

    @Override
    public TrackResult getTrackNo(KongBaoParam kongBaoParam) throws Exception {
        // 从数据库获取
        TrackResult result = trackNoByMysql.getByMysql(kongBaoParam);

        if (result.getSuccess()) {
            return result;
        }

        // 从空包网获取
        return this.trackNoByKongBao.getTrackNoByKongBao(kongBaoParam);
    }
}
