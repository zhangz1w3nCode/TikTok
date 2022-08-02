package com.zzw.controller;

import com.zzw.base.BaseInfoProperties;
import com.zzw.bo.RegisterLoginBO;
import com.zzw.bo.VlogBO;
import com.zzw.enums.YesOrNo;
import com.zzw.grace.result.GraceJSONResult;
import com.zzw.grace.result.ResponseStatusEnum;
import com.zzw.pojo.Users;
import com.zzw.service.vlogService;
import com.zzw.utils.IPUtil;
import com.zzw.utils.PagedGridResult;
import com.zzw.utils.SMSUtils;
import com.zzw.vo.IndexVlogVO;
import com.zzw.vo.UsersVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Api(tags = "视频接口")
@RequestMapping("vlog")
@RestController
@CrossOrigin
@Slf4j
public class vlogController extends BaseInfoProperties {

    @Autowired
    private com.zzw.service.vlogService vlogService;

    @Autowired
    private com.zzw.service.imp.userServiceImp userServiceImp;

    //上传视频成功后-保存视频信息到数据库
    @PostMapping("publish")
    public GraceJSONResult publish(@RequestBody VlogBO vlogBO){

        //Fixme:校验 vlogbo的参数
        vlogService.creatVlog(vlogBO);

        return GraceJSONResult.ok("上传视频成功");
    }

    //获取视频信息列表 通过关键字进行模糊搜索-
    //todo 可以使用es去优化 因为 这里用了 'like %xxxx%' 无法用索引;
    @GetMapping("indexList")
    public GraceJSONResult indexList(@RequestParam String userId,
                                     @RequestParam(defaultValue = "") String search,
                                     @RequestParam Integer page,
                                     @RequestParam Integer pageSize){
        if(page==null) page = COMMON_START_PAGE;
        if(pageSize ==null) pageSize  =COMMON_PAGE_SIZE;
        PagedGridResult pagedGridResult = vlogService.getIndexVlogList(userId,search, page, pageSize);
        return GraceJSONResult.ok(pagedGridResult);
    }
    //全局搜索后-得出的视频列表--进行点击后能进入详情页面
    @GetMapping("detail")
    public GraceJSONResult indexList(@RequestParam(defaultValue = "") String userId,
                                     @RequestParam String vlogId){


        IndexVlogVO IndexVlogVO = vlogService.getDetailByVlogId(vlogId);

        return GraceJSONResult.ok(IndexVlogVO);
    }
    //把视频设置为私密视频---只有自己才能把自己的视频改为私密
    @PostMapping("changeToPrivate")
    public GraceJSONResult changeToPrivate(@RequestParam String userId,
                                     @RequestParam String vlogId){

        vlogService.changeToPrivateOrPublic(userId,vlogId, YesOrNo.YES.type);

        return GraceJSONResult.ok();
    }
    //把视频设置为私密视频---只有自己才能把自己的视频改为私密
    @PostMapping("changeToPublic")
    public GraceJSONResult changeToPublic(@RequestParam String userId,
                                           @RequestParam String vlogId){

        vlogService.changeToPrivateOrPublic(userId,vlogId, YesOrNo.NO.type);

        return GraceJSONResult.ok();
    }
    //我的作品--我的公开视频
    @GetMapping("myPublicList")
    public GraceJSONResult myPublicList(@RequestParam String userId,
                                        @RequestParam Integer page,
                                        @RequestParam Integer pageSize){

        if(page==null) page = COMMON_START_PAGE;

        if(pageSize ==null) pageSize  =COMMON_PAGE_SIZE;


        PagedGridResult pagedGridResult = vlogService.queryMyVlogList(userId,YesOrNo.NO.type,page, pageSize);

        return GraceJSONResult.ok(pagedGridResult);
    }
    //我的作品--我的私密视频
    @GetMapping("myPrivateList")
    public GraceJSONResult myPrivateList(@RequestParam String userId,
                                        @RequestParam Integer page,
                                        @RequestParam Integer pageSize){

        if(page==null) page = COMMON_START_PAGE;

        if(pageSize ==null) pageSize  =COMMON_PAGE_SIZE;


        PagedGridResult pagedGridResult = vlogService.queryMyVlogList(userId,YesOrNo.YES.type,page, pageSize);

        return GraceJSONResult.ok(pagedGridResult);
    }
    //我的喜欢列表
    @GetMapping("myLikedList")
    public GraceJSONResult myLikedList(@RequestParam String userId,
                                         @RequestParam Integer page,
                                         @RequestParam Integer pageSize){

        if(page==null) page = COMMON_START_PAGE;

        if(pageSize ==null) pageSize  =COMMON_PAGE_SIZE;


        PagedGridResult pagedGridResult = vlogService.getMyLikedList(userId,page, pageSize);

        return GraceJSONResult.ok(pagedGridResult);
    }
    //我的关注列表
    @GetMapping("followList")
    public GraceJSONResult followList(@RequestParam String myId,
                                       @RequestParam Integer page,
                                       @RequestParam Integer pageSize){

        if(page==null) page = COMMON_START_PAGE;

        if(pageSize ==null) pageSize  =COMMON_PAGE_SIZE;

        PagedGridResult pagedGridResult = vlogService.getMyFollowList(myId,page, pageSize);

        return GraceJSONResult.ok(pagedGridResult);
    }


    //点赞
    @PostMapping("like")
    public GraceJSONResult like(@RequestParam String userId,
                                @RequestParam String vlogerId,
                                @RequestParam String vlogId){
        //todo 根据id 查询 用户是否存在 博主是否存在 视频是否存在

        //mysql
        vlogService.userLikedVlog(userId,vlogId);
        //redis 点赞后视频和视频发布者都+1
        redis.increment(REDIS_VLOG_BE_LIKED_COUNTS+":"+vlogId,1);
        redis.increment(REDIS_VLOGER_BE_LIKED_COUNTS+":"+vlogerId,1);


        //关联关系  -- 用户喜欢哪个 视频
        redis.set(REDIS_USER_LIKE_VLOG+":"+userId+":"+vlogId,"1");

        return GraceJSONResult.ok();
    }
    //取消点赞
    @PostMapping("unlike")
    public GraceJSONResult userUnlikeVlog(@RequestParam String userId,
                                @RequestParam String vlogerId,
                                @RequestParam String vlogId){
        //todo 根据id 查询 用户是否存在 博主是否存在 视频是否存在

        //mysql
        vlogService.userUnlikeVlog(userId,vlogId);

        //redis 点赞后视频和视频发布者都+1
        redis.decrement(REDIS_VLOG_BE_LIKED_COUNTS+":"+vlogId,1);
        redis.decrement(REDIS_VLOGER_BE_LIKED_COUNTS+":"+vlogerId,1);


        //关联关系  -- 用户喜欢哪个 视频
        redis.del(REDIS_USER_LIKE_VLOG+":"+userId+":"+vlogId);

        return GraceJSONResult.ok();
    }
    //获得视频点赞总数--每次刷到视频就去查询 点赞记录
    @PostMapping("totalLikedCounts")
    public GraceJSONResult totalLikedCounts(@RequestParam String vlogId){

        //mysql
        Integer total = vlogService.getVlogLikeCount(vlogId);

        return GraceJSONResult.ok(total);
    }

}
