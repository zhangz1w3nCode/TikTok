package com.zzw.controller;

import com.zzw.base.BaseInfoProperties;
import com.zzw.bo.CommentBO;
import com.zzw.grace.result.GraceJSONResult;

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


    @ApiOperation("获取评论")
    @GetMapping("list")
    public GraceJSONResult list(@RequestParam String vlogId,
                                @RequestParam  String userId,
                                @RequestParam Integer page,
                                @RequestParam Integer pageSize){
        PagedGridResult commentList = commentService.getCommentList(userId,vlogId, page, pageSize);

        return GraceJSONResult.ok(commentList);
    }


    @ApiOperation("删除评论操作")
    @DeleteMapping("delete")
    public GraceJSONResult delete(@RequestParam String commentUserId,
                                @RequestParam String commentId,
                                @RequestParam String vlogId){
        commentService.deleteComment(commentUserId,commentId,vlogId);

        return GraceJSONResult.ok();
    }

    @ApiOperation("点赞评论操作")
    @PostMapping("like")
    public GraceJSONResult like(@RequestParam String commentId,
                                  @RequestParam String userId){
        //纯redis 操作 不涉及数据库

        commentService.addToMongoDB(commentId,userId);
        //这条评论的总数
        redis.increment(REDIS_VLOG_COMMENT_LIKED_COUNTS+":"+commentId,1);
        //哪个用户点赞了这条评论
        redis.set(REDIS_USER_LIKE_COMMENT+":"+userId+":"+commentId,"1");

        return GraceJSONResult.ok();
    }

    @ApiOperation("取消点赞评论操作")
    @PostMapping("unlike")
    public GraceJSONResult unlike(@RequestParam String commentId,
                                @RequestParam String userId){

        redis.decrement(REDIS_VLOG_COMMENT_LIKED_COUNTS+":"+commentId,1);
        redis.del(REDIS_USER_LIKE_COMMENT+":"+userId+":"+commentId);

        return GraceJSONResult.ok();
    }

}
