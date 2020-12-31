package com.somecom.thymeleaf.config;

import com.somecom.thymeleaf.TimoDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Sam
 * @date 2018/8/14
 */
@Configuration
public class ThymeleafConfig {

    /**
     * 配置自定义的CusDialect，用于整合thymeleaf模板
     */
    @Bean
    public TimoDialect getTimoDialect() {
        return new TimoDialect();
    }

}
