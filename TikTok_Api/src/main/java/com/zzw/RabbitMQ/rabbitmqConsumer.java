package com.zzw.RabbitMQ;

import com.alibaba.fastjson.JSONObject;
import com.zzw.enums.MessageEnum;
import com.zzw.grace.exceptions.GraceException;
import com.zzw.mo.messageMO;
import com.zzw.rabbitmqConfig;
import com.zzw.utils.JsonUtils;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class rabbitmqConsumer {
    @Autowired
    private com.zzw.service.msgService msgService;
    //消息的监听
    @RabbitListener(queues ={rabbitmqConfig.QUEUE_MSG})
    public void msgListener(String payLoad,Message msg){
        log.info("发送的消息："+payLoad);

        messageMO messageMO = JsonUtils.jsonToPojo(payLoad, messageMO.class);

        //根据 路由 routing key 去区分生产者消息的类型  再入到mongodb库中
        String routingKey = msg.getMessageProperties().getReceivedRoutingKey();

        //如果是关注消息
        if(routingKey.equalsIgnoreCase("system.msg."+MessageEnum.FOLLOW_YOU.value)){
            msgService.creatMsg(messageMO.getFromUserId(),messageMO.getToUserId(), MessageEnum.FOLLOW_YOU.type,null);
        }else if(routingKey.equalsIgnoreCase("system.msg."+MessageEnum.LIKE_VLOG.value)){//MessageEnum.LIKE_VLOG.type
            msgService.creatMsg(messageMO.getFromUserId(),messageMO.getToUserId(), MessageEnum.LIKE_VLOG.type,messageMO.getMsgContent());
        }else if(routingKey.equalsIgnoreCase("system.msg."+MessageEnum.COMMENT_VLOG.value)){//MessageEnum.LIKE_VLOG.type
            msgService.creatMsg(messageMO.getFromUserId(),messageMO.getToUserId(), MessageEnum.COMMENT_VLOG.type,messageMO.getMsgContent());
        }else if(routingKey.equalsIgnoreCase("system.msg."+MessageEnum.REPLY_YOU.value)){//MessageEnum.LIKE_VLOG.type
            msgService.creatMsg(messageMO.getFromUserId(),messageMO.getToUserId(), MessageEnum.REPLY_YOU.type,messageMO.getMsgContent());
        }else if(routingKey.equalsIgnoreCase("system.msg."+MessageEnum.LIKE_COMMENT.value)){//MessageEnum.LIKE_VLOG.type
            msgService.creatMsg(messageMO.getFromUserId(),messageMO.getToUserId(), MessageEnum.LIKE_COMMENT.type,messageMO.getMsgContent());
        }else{

        }


    }


}
