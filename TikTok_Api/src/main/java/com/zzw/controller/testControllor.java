package com.zzw.controller;

import com.zzw.grace.result.GraceJSONResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testControllor {


    @GetMapping("/test")
    public Object test(){
        return GraceJSONResult.ok("Hello,Word!");
    }

    @GetMapping("/hello")
    public String tt(){
        return "zry";
    }

}
