package com.zzw;

import com.zzw.intercepter.PassportInterceptor;
import com.zzw.intercepter.userTokenInterceptor;
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

    public userTokenInterceptor getUserTokenInterceptor(){
        return new userTokenInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(getPassportInterceptor())
                    .addPathPatterns("/passport/getSMSCode");
            //registry.addInterceptor(getUserTokenInterceptor())
                    //.addPathPatterns("/userInfo/modifyUserInfo")
                    //.addPathPatterns("/userInfo/modifyImage");
    }


}
