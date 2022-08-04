package com.zzw.testController;

import com.zzw.base.BaseInfoProperties;
import com.zzw.bo.CommentBO;
import com.zzw.grace.result.GraceJSONResult;
import com.zzw.rabbitmqConfig;
import com.zzw.utils.PagedGridResult;
import com.zzw.vo.CommentVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.amqp.core.*;


@Api(tags = "rabbitmq测试接口")
@RestController
@CrossOrigin
@Slf4j
public class rabbitmqTestController extends BaseInfoProperties {



    @Autowired
    public RabbitTemplate rabbitTemplate;

    //创建一条评论
    @ApiOperation("测试发送消息到mq到接口")
    @GetMapping("mqsent")
    public GraceJSONResult creatComment(String msg){
        String exchange = rabbitmqConfig.EXCHANGE_MSG;
        rabbitTemplate.convertAndSend(exchange,"system.msg.sent","我爱曾如玉");
        return GraceJSONResult.ok();
    }



}
