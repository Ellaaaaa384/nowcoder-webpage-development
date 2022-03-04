package com.nowcoder.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

@Configuration
public class AlphaConfig {

    @Bean //定义第三方bean
    public SimpleDateFormat simpleDateFormat(){ //灰色小写字是方法名，也是bean的名字
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
}
