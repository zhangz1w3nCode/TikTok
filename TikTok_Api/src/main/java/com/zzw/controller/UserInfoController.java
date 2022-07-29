package com.zzw.controller;

import com.zzw.ao.UsersVO;
import com.zzw.bo.RegisterLoginBO;
import com.zzw.bo.UpdateUserBO;
import com.zzw.enums.UserInfoModifyType;
import com.zzw.grace.result.GraceJSONResult;
import com.zzw.grace.result.ResponseStatusEnum;
import com.zzw.pojo.Users;
import com.zzw.utils.IPUtil;
import com.zzw.utils.SMSUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

@Api(tags = "用户个人中心接口")
@RequestMapping("userInfo")
@RestController
@CrossOrigin
@Slf4j
// url: serverUrl + "/userInfo/query?userId=" + myUserId,
public class UserInfoController extends BaseInfoProperties {


    @Autowired
    private com.zzw.service.imp.userServiceImp userServiceImp;


    @GetMapping("/query")
    public Object getUserInfo(@RequestParam String userId) {

        Users users = userServiceImp.queryUserInfo(userId);

        //对象拷贝
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(users,usersVO);

        //获取我的关注 我的粉丝 我的获赞 用redis去做

        String myFansStr = redis.get(REDIS_MY_FANS_COUNTS + ":" + userId);
        String myFollowsStr = redis.get(REDIS_MY_FOLLOWS_COUNTS + ":" + userId);
        String myVlogLikeStr = redis.get(REDIS_VLOG_BE_LIKED_COUNTS + ":" + userId);
        String LikeMeCntStr = redis.get(REDIS_VLOGER_BE_LIKED_COUNTS + ":" + userId);

        Integer myFans =0;
        Integer myFollows = 0;
        Integer myVlogLike =0;
        Integer LikeMeCnt =0;
        Integer TotalLike =0;

        if(StringUtils.isNotBlank(myFansStr)){
            myFans = Integer.valueOf(myFansStr);
        }
        if(StringUtils.isNotBlank(myFollowsStr)){
            myFollows = Integer.valueOf(myFollowsStr);
        }
        if(StringUtils.isNotBlank(myVlogLikeStr)){
            myVlogLike = Integer.valueOf(myVlogLikeStr);
        }
        if(StringUtils.isNotBlank(LikeMeCntStr)){
            LikeMeCnt = Integer.valueOf(LikeMeCntStr);
        }

        TotalLike = myVlogLike+LikeMeCnt;

        usersVO.setMyFansCounts(myFans);
        usersVO.setMyFollowsCounts(myFollows);
        usersVO.setTotalLikeMeCounts(TotalLike);

        return GraceJSONResult.ok(usersVO);
    }

    @PostMapping("/modifyUserInfo")
    public Object updateUserInfo(@RequestBody UpdateUserBO UpdateUserBO
    ,@RequestParam Integer type) {

        // 修改不能 和数据库中的数据一样
        //通过修改的选项不同 那么 修改的方式也不同 通过type去区分

        UserInfoModifyType.checkUserInfoTypeIsRight(type);

        Users newestUsers = userServiceImp.updateUserInfo(UpdateUserBO,type);

        return GraceJSONResult.ok(newestUsers);
    }

}
