package com.zzw.controller;

import com.zzw.MinIOConfig;
import com.zzw.base.BaseInfoProperties;
import com.zzw.vo.UsersVO;
import com.zzw.bo.UpdateUserBO;
import com.zzw.enums.FileTypeEnum;
import com.zzw.enums.UserInfoModifyType;
import com.zzw.grace.result.GraceJSONResult;
import com.zzw.grace.result.ResponseStatusEnum;
import com.zzw.pojo.Users;
import com.zzw.utils.MinIOUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "用户个人中心接口")
@RequestMapping("/userInfo")
@RestController
@CrossOrigin
@Slf4j
// url: serverUrl + "/userInfo/query?userId=" + myUserId,
public class UserInfoController extends BaseInfoProperties {


    @Autowired
    private com.zzw.service.userService userServiceImp;

    @Autowired
    private MinIOConfig minIOConfig;

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


    @PostMapping("/modifyImage")
    public GraceJSONResult modifyImage(@RequestParam String userId,
                                       @RequestParam Integer type,
                                       MultipartFile file,
                                       HttpServletRequest request) throws Exception {

        if(type!=FileTypeEnum.BGIMG.type&&type!=FileTypeEnum.FACE.type){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }

        String bucketName = minIOConfig.getBucketName();
        String filename = file.getOriginalFilename();

        MinIOUtils.uploadFile(bucketName,filename,file.getInputStream());

        String url = minIOConfig.getFileHost()+"/"+bucketName+"/"+filename;

        UpdateUserBO UserBO = new UpdateUserBO();

        UserBO.setId(userId);

        if (type==FileTypeEnum.FACE.type){
            UserBO.setFace(url);

        }else{
            UserBO.setBgImg(url);
        }

        Users afterSetUser = userServiceImp.updateUserInfo(UserBO);

        String userxId = request.getHeader("headerUserId");
        String userToken = request.getHeader("headerUserToken");

        System.out.println("userId:"+userxId);
        System.out.println("userToken:"+userToken);
        System.out.println("--------------------------");



        return GraceJSONResult.ok(afterSetUser);
    }

}
