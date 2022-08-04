package com.zzw.controller;

import com.zzw.base.BaseInfoProperties;
import com.zzw.bo.CommentBO;
import com.zzw.grace.result.GraceJSONResult;
import com.zzw.mo.messageMO;
import com.zzw.utils.PagedGridResult;
import com.zzw.vo.CommentVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "消息接口")
@RequestMapping("msg")
@RestController
@CrossOrigin
@Slf4j
public class messageController extends BaseInfoProperties {

    @Autowired
    private com.zzw.service.msgService msgService;



    @ApiOperation("获取评论")
    @GetMapping("list")
    public GraceJSONResult list(@RequestParam String userId,
                                @RequestParam Integer page,
                                @RequestParam Integer pageSize){

        if(page==null){
            page = COMMON_START_PAGE_ZERO;
        }

        if(pageSize==null){
            pageSize = COMMON_PAGE_SIZE;
        }

       List<messageMO> commentList =msgService.getCommentList(userId,page,pageSize);

        return GraceJSONResult.ok(commentList);
    }


}
