package com.income.robot.service.strategy;

import com.income.robot.code.entity.PddKonbbaoInfo;
import com.income.robot.service.process.TrackResult;

import java.io.IOException;

/**
 * 
 * @author wanghuan
 *
 */
public interface IKongBaoStragegy {

	/**
	 * 获取运单号（从空包网）
	 * @param kongBaoParam
	 * @return
	 */
	TrackResult generateTrackNo(KongBaoParam kongBaoParam, PddKonbbaoInfo konbbaoAccount);
}
