package com.zzw.controller;

import com.zzw.grace.result.GraceJSONResult;
import com.zzw.utils.IPUtil;
import com.zzw.utils.SMSUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;

@Api(tags = "短信接口")
@RequestMapping("passport")
@RestController
@CrossOrigin
@Slf4j
//  '/passport/getSMSCode?mobile='
public class PassportController extends BaseInfoProperties {

    @Autowired
    private SMSUtils smsUtils;

    @PostMapping("getSMSCode")
    public Object getSMSCode(@RequestParam String mobile,
                             HttpServletRequest httpServletRequest) throws Exception {

        if(StringUtils.isBlank(mobile)){
            return GraceJSONResult.ok();
        }

        //限制用户 每60秒 才能 获得一个验证码
        String ip = IPUtil.getRequestIp(httpServletRequest);

        //给用户的IP 设置一个过期时间-超过了不能发送验证码了
        redis.setnx60s(MOBILE_SMSCODE+":"+ip,ip);

        //生产验证码
        String code = String.valueOf((int)((Math.random() * 9 + 1) * 100000));
        log.info(code);

        //todo:等审核成功记得去测试短信接口
        //todo:把验证码发送到用户手机
        //smsUtils.sendSMS(mobile,code);

        //把验证码存到redis-3分钟过期
        redis.set(MOBILE_SMSCODE+":"+mobile,code,30*60);

        return GraceJSONResult.ok();
    }


}
