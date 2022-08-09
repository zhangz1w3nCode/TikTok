package com.zzw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@MapperScan(basePackages = "com.zzw.mapper")
@ComponentScan(basePackages = {"com.zzw","org.n3r.idworker"})
@EnableMongoRepositories
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
        Map<Long,Integer> map = new HashMap<>();

        Integer integer = map.get("1");
        
    }
}
