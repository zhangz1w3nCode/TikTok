package com.zzw;

import com.zzw.intercepter.PassportInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    public PassportInterceptor getPassportInterceptor(){
        return new PassportInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(getPassportInterceptor()).addPathPatterns("/passport/getSMSCode");
    }
}
