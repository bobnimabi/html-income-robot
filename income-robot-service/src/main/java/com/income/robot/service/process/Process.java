package com.income.robot.service.process;

import com.income.robot.service.strategy.KongBaoParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 获取TrackNo流程
 */
@Slf4j
@Service
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
        log.info("数据库获取运单号->失败，原因：{}", result.getError());

        // 从空包网获取
        result = this.trackNoByKongBao.getTrackNoByKongBao(kongBaoParam);
        if (!result.getSuccess()) {
            log.error("空包网获取运单号->失败，原因：{}", result.getError());
        }

        return result;
    }
}
