package com.zzw;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class rabbitmqConfig {


    //构建 交换机 队列 以及 映射路径

    public static final String EXCHANGE_MSG = "exchange_msg";
    public static final String QUEUE_MSG = "queue_msg";

    public static final String ROUTING_KEY = "system.msg.";


    @Bean(EXCHANGE_MSG)
    public Exchange exchange(){
        return ExchangeBuilder.topicExchange(EXCHANGE_MSG)
                .durable(true)
                .build();
    }

    @Bean(QUEUE_MSG)
    public Queue Queue(){
        return new Queue(QUEUE_MSG);
    }


    @Bean
    public Binding bind(@Qualifier(EXCHANGE_MSG) Exchange exchange,
                        @Qualifier(QUEUE_MSG) Queue queue){
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("system.msg.*")
                .noargs();
    }
}
