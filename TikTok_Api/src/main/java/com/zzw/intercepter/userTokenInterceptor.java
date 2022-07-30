package com.zzw.intercepter;

import com.zzw.controller.BaseInfoProperties;
import com.zzw.grace.exceptions.GraceException;
import com.zzw.grace.result.ResponseStatusEnum;
import com.zzw.utils.IPUtil;
import com.zzw.utils.tokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class userTokenInterceptor extends BaseInfoProperties implements HandlerInterceptor {

    //对token进行拦截验证
    //用户登录成功后 会生成一个token 如果还要对后续的请求进行访问的话 就需要token的校验(不是存在本地而是存在redis中)(分布式操作)
    //
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {


        // 从header中获得用户id和token
        String userId = request.getHeader("headerUserId");
        String userToken = request.getHeader("headerUserToken");

        System.out.println("userId:"+userId);
        System.out.println("headerUserToken:"+userToken);

        // 判断header中用户id和token不能为空
        if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(userToken)) {
            String redisToken = redis.get(REDIS_USER_TOKEN + ":" + userId);
            if (StringUtils.isBlank(redisToken)) {
                GraceException.display(ResponseStatusEnum.UN_LOGIN);
                return false;
            } else {
                // 比较token是否一致，如果不一致，表示用户在别的手机端登录
                if (!redisToken.equalsIgnoreCase(userToken)) {
                    GraceException.display(ResponseStatusEnum.TICKET_INVALID);
                    return false;
                }
            }
        } else {
            GraceException.display(ResponseStatusEnum.UN_LOGIN);
            return false;
        }

        /**
         * true: 请求放行
         * false: 请求拦截
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
