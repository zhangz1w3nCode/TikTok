package com.zzw.controller;

import com.zzw.ao.UsersVO;
import com.zzw.bo.RegisterLoginBO;
import com.zzw.grace.result.GraceJSONResult;
import com.zzw.grace.result.ResponseStatusEnum;
import com.zzw.pojo.Users;
import com.zzw.service.imp.userServiceImp;
import com.zzw.service.userService;
import com.zzw.utils.IPUtil;
import com.zzw.utils.SMSUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@Api(tags = "短信接口")
@RequestMapping("passport")
@RestController
@CrossOrigin
@Slf4j
//  '/passport/getSMSCode?mobile='
public class PassportController extends BaseInfoProperties {

    @Autowired
    private SMSUtils smsUtils;

    @Autowired
    private com.zzw.service.imp.userServiceImp userServiceImp;


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

        //把验证码存到redis-3分钟过期--
        redis.set(MOBILE_SMSCODE+":"+mobile,code,30*60);

        return GraceJSONResult.ok();
    }

    @PostMapping("login")
    public Object login(@Valid @RequestBody RegisterLoginBO registerLoginBO,
                        //BindingResult result,--验证判断 让 GraceExceptionHandler 去做
                        HttpServletRequest HttpServletRequest) throws Exception {

        log.error(registerLoginBO.getMobile());
        log.error(registerLoginBO.getSmsCode());

        String mobile = registerLoginBO.getMobile();
        String smsCode = registerLoginBO.getSmsCode();

        String redisCode = redis.get(MOBILE_SMSCODE + ":" + mobile);

        //验证码校验
        if(StringUtils.isBlank(redisCode)||!redisCode.equalsIgnoreCase(smsCode))
        {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SMS_CODE_ERROR);
        }

        //用户校验-不存在用户则创建用户 存在的话继续登入主页

        Users users = userServiceImp.queryUserIsExit(mobile);

        if(users==null){//不存在用户-创建用户
            users = userServiceImp.creatUserIs(mobile);
        }

        String token = UUID.randomUUID().toString();

        redis.set(REDIS_USER_TOKEN+":"+users.getId(),token);

        redis.del(MOBILE_SMSCODE + ":" + mobile);

        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(users,usersVO);
        usersVO.setUserToken(token);

        return GraceJSONResult.ok(usersVO);
    }

    //退出登录方法
    @PostMapping("logout")
    public Object logout(@RequestParam String userId){

        redis.del(REDIS_USER_TOKEN+":"+userId);



        return GraceJSONResult.ok("退出登录成功");
    }

}
