package com.zzw.intercepter;

import com.zzw.controller.BaseInfoProperties;
import com.zzw.grace.exceptions.GraceException;
import com.zzw.grace.result.ResponseStatusEnum;
import com.zzw.utils.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class PassportInterceptor extends BaseInfoProperties implements HandlerInterceptor {



    //发送验证码接口的拦截器-一个ip地址的用户不能 在60内再次请求验证码 防止暴力请求接口
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String ip = IPUtil.getRequestIp(request);

        boolean keyIsExist = redis.keyIsExist(MOBILE_SMSCODE + ":" + ip);

        if(keyIsExist){//存在说明是第二次访问了 应该做限制
            GraceException.display(ResponseStatusEnum.SMS_NEED_WAIT_ERROR);
            //log.info("点击太频繁！");
            return false;
        }

        /*
           true 表示拦截器放行 不会被拦截
           false 表示会被拦截
         */

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
