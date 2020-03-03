package com.income.robot.code.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.income.robot.code.entity.PddDict;
import com.income.robot.code.mapper.PddDictMapper;
import com.income.robot.code.service.IPddDictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 站点配置项 服务实现类
 * </p>
 *
 * @author admin
 * @since 2020-02-26
 */
@Service
public class PddDictServiceImpl extends ServiceImpl<PddDictMapper, PddDict> implements IPddDictService {
    public String getDict(String key) {
        PddDict dict = getOne(new LambdaQueryWrapper<PddDict>()
                .eq(PddDict::getDictKey, key));
        return dict.getDictValue();
    }

}
