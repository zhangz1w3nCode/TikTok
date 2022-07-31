package com.zzw;

import com.zzw.intercepter.PassportInterceptor;
import com.zzw.intercepter.UserTokenInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@CrossOrigin
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    public PassportInterceptor getPassportInterceptor(){
        return new PassportInterceptor();
    }

    @Bean
    public UserTokenInterceptor userTokenInterceptor() {
        return new UserTokenInterceptor();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(getPassportInterceptor())
                    .addPathPatterns("/passport/getSMSCode");
           //registry.addInterceptor(userTokenInterceptor())
                //.addPathPatterns("/userInfo/modifyUserInfo")
                //.addPathPatterns("/userInfo/modifyImage");

        //registry.addInterceptor(userTokenInterceptor())
                //.addPathPatterns("/userInfo/modifyUserInfo");
                //.addPathPatterns("/userInfo/modifyImage");
    }


}
