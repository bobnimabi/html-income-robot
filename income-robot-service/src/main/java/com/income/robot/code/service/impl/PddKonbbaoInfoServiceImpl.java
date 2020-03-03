package com.income.robot.code.service.impl;

import com.income.robot.code.entity.PddKonbbaoInfo;
import com.income.robot.code.mapper.PddKonbbaoInfoMapper;
import com.income.robot.code.service.IPddKonbbaoInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 空包网账号信息 服务实现类
 * </p>
 *
 * @author admin
 * @since 2020-03-02
 */
@Service
public class PddKonbbaoInfoServiceImpl extends ServiceImpl<PddKonbbaoInfoMapper, PddKonbbaoInfo> implements IPddKonbbaoInfoService {
    /**
     * 获取空包网的账号信息
     */
    public PddKonbbaoInfo getKongBaoInfo() {
        PddKonbbaoInfo info = getOne(null);
        if (null == info) {
            throw new IllegalStateException("未配置空包网信息");
        } else if (StringUtils.isEmpty(info.getAccount()) || StringUtils.isEmpty(info.getPassword())
                || StringUtils.isEmpty(info.getApiUrl()) || StringUtils.isEmpty(info.getStrategy())) {
            throw new IllegalStateException("空包网信息配置不全：账号、密码、apiUrl、策略");
        }
        return info;
    }
}
