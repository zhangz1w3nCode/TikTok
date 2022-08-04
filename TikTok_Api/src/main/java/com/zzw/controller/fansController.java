package com.zzw.controller;

import com.zzw.base.BaseInfoProperties;
import com.zzw.bo.VlogBO;
import com.zzw.enums.YesOrNo;
import com.zzw.grace.result.GraceJSONResult;
import com.zzw.grace.result.ResponseStatusEnum;
import com.zzw.pojo.Users;
import com.zzw.utils.PagedGridResult;
import com.zzw.vo.IndexVlogVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "关注接口")
@RequestMapping("fans")
@RestController
@CrossOrigin
@Slf4j
public class fansController extends BaseInfoProperties {

    @Autowired
    private com.zzw.service.fansService fansService;

    @Autowired
    private com.zzw.service.userService userService;

    //关注博主
    @PostMapping("follow")
    public GraceJSONResult follow(@RequestParam String myId,
                                  @RequestParam String vlogerId){

        if(StringUtils.isBlank(myId) ||StringUtils.isBlank(vlogerId) ){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
        }

        if(myId.equalsIgnoreCase(vlogerId)){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_RESPONSE_NO_INFO);
        }

        Users me = userService.queryUserInfo(myId);
        Users vloger = userService.queryUserInfo(vlogerId);

        if(me==null||vloger==null){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_RESPONSE_NO_INFO);
        }


        fansService.creatFans(myId,vlogerId);



        //关注博主后-页面的 粉丝-关注也要改变----用redis去做
        //我的关注+1 然后博主的粉丝+1
        redis.increment (REDIS_MY_FOLLOWS_COUNTS+":"+myId,1);
        redis.increment (REDIS_MY_FANS_COUNTS+":"+vlogerId,1);

        //设置一个键-作为互粉的连接--表示 我-关注了-谁
        redis.set(REDIS_FANS_AND_VLOGGER_RELATIONSHIP+":"+myId+":"+vlogerId,"1");




        return GraceJSONResult.ok();
    }

    //取消关注
    @PostMapping("cancel")
    public GraceJSONResult cancel(@RequestParam String myId,
                                  @RequestParam String vlogerId){

        fansService.cancelFollow(myId,vlogerId);

        //关注博主后-页面的 粉丝-关注也要改变----用redis去做
        //我的关注+1 然后博主的粉丝+1
        redis.decrement (REDIS_MY_FOLLOWS_COUNTS+":"+myId,1);
        redis.decrement (REDIS_MY_FANS_COUNTS+":"+vlogerId,1);

        //设置一个键-作为互粉的连接--表示 我-关注了-谁
        redis.del(REDIS_FANS_AND_VLOGGER_RELATIONSHIP+":"+myId+":"+vlogerId);

        return GraceJSONResult.ok();
    }


    //查看我是否关注了博主
    @GetMapping("queryDoIFollowVloger")
    public GraceJSONResult queryDoIFollowVloger(@RequestParam String myId,
                                  @RequestParam String vlogerId){
        return GraceJSONResult.ok(fansService.queryDoIFollowVloger(myId,vlogerId));
    }

    //我的关注列表
    @GetMapping("queryMyFollows")
    public GraceJSONResult queryMyFollows(@RequestParam String myId,
                                          @RequestParam Integer page,
                                          @RequestParam Integer pageSize){

        PagedGridResult res = fansService.queryMyVlogList(myId, page, pageSize);

        return GraceJSONResult.ok(res);
    }

    //我的粉丝列表
    @GetMapping("queryMyFans")
    public GraceJSONResult queryMyFans(@RequestParam String myId,
                                       @RequestParam Integer page,
                                       @RequestParam Integer pageSize){

        PagedGridResult res = fansService.getMyFansList(myId, page, pageSize);

        return GraceJSONResult.ok(res);
    }
}
