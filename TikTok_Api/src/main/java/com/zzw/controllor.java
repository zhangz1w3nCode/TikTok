package com.zzw;

import com.zzw.grace.result.GraceJSONResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class controllor {


    @GetMapping("/test")
    public Object test(){
        return GraceJSONResult.ok("Hello,Word!");
    }


}
