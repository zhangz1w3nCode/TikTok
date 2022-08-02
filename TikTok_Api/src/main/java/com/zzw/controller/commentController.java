package com.zzw.controller;

import com.tencentcloudapi.bm.v20180423.models.CpuInfo;
import com.zzw.base.BaseInfoProperties;
import com.zzw.bo.CommentBO;
import com.zzw.grace.result.GraceJSONResult;
import com.zzw.grace.result.ResponseStatusEnum;
import com.zzw.pojo.Users;
import com.zzw.utils.PagedGridResult;
import com.zzw.vo.CommentVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "评论接口")
@RequestMapping("comment")
@RestController
@CrossOrigin
@Slf4j
public class commentController extends BaseInfoProperties {

    @Autowired
    private com.zzw.service.commentService commentService;


    //创建一条评论
    @ApiOperation("创建评论方法")
    @PostMapping("create")
    public GraceJSONResult creatComment(@RequestBody @Valid CommentBO commentBO){
        /*
           todo：
           校验fatherCommentId，这个在不为0的情况下，
           表示是存在的，所以需要校验父评论记录是否存在，
           如果不存在，返回错误
         */
        /*
            todo
            vlogerId 和 commentUserId 都是对应用户的主键，
            所以一定要校验是否真实存在用户，不存在抛出异常。这里之前我们有写过，
            这边建议进阶一下，不要直接复制代码，我们可以对代码进行封装，
            封装为统一方法专门用于校验，比如[ checkUserExist(userId) ]，
            直接在代码中加入一行即可，更加直观便于阅读，也有解耦的目的。
         */
        CommentVO commentVO = commentService.creatComment(commentBO);

        return GraceJSONResult.ok(commentVO);
    }

    @ApiOperation("去redis中获取评论总数")
    @GetMapping("counts")
    public GraceJSONResult counts(@RequestParam String vlogId){

        String count = redis.get(REDIS_VLOG_COMMENT_COUNTS + ":" + vlogId);

        if(StringUtils.isBlank(count)){
            count = "0";
        }

        return GraceJSONResult.ok(Integer.parseInt(count));
    }


   }
