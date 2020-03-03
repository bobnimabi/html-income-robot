package com.income.robot.server.config;

import com.income.robot.service.config.MybatisPlusConfigBase;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by mrt on 11/18/2019 2:31 PM
 */
@EnableTransactionManagement
@Configuration
public class MybatisPlusConfig extends MybatisPlusConfigBase {

}
