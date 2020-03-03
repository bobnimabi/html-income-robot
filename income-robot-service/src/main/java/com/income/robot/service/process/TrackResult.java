package com.income.robot.service.process;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by mrt on 2020/3/2 12:18
 *包装类：调用 "空包网" 或 查询数据库 返回的运单号对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrackResult {

    /**
     * 响应是否成功
     */
    private Boolean success;

    /**
     * 运单号
     */
    private String trackNo;

    /**
     * 错误信息
     */
    private String error;
}
