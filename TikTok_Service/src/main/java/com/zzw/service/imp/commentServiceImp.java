package com.zzw.service.imp;

import com.github.pagehelper.PageHelper;
import com.zzw.base.BaseInfoProperties;
import com.zzw.bo.CommentBO;
import com.zzw.enums.YesOrNo;
import com.zzw.mapper.CommentMapper;
import com.zzw.mapper.FansMapper;
import com.zzw.mapper.FansMapperDIY;
import com.zzw.pojo.Comment;
import com.zzw.pojo.Fans;
import com.zzw.service.commentService;
import com.zzw.service.fansService;
import com.zzw.utils.PagedGridResult;
import com.zzw.vo.CommentVO;
import com.zzw.vo.FansVO;
import com.zzw.vo.VlogerVO;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class commentServiceImp extends BaseInfoProperties implements commentService {

    @Autowired
    private CommentMapper CommentMapper;


    @Autowired
    private  Sid sid;


    @Transactional
    @Override
    public CommentVO creatComment(CommentBO commentBO) {//我 关注 别人  -- 我是粉丝
        String Id = sid.nextShort();
        Comment comment = new Comment();

        comment.setId(Id);
        comment.setVlogId(commentBO.getVlogId());
        comment.setVlogerId(commentBO.getVlogerId());
        comment.setCommentUserId(commentBO.getCommentUserId());
        comment.setFatherCommentId(commentBO.getFatherCommentId());
        comment.setContent(commentBO.getContent());
        comment.setLikeCounts(0);
        comment.setCreateTime(new Date());


        //插入数据库
        CommentMapper.insert(comment);

        //插入redis
        redis.increment(REDIS_VLOG_COMMENT_COUNTS+":"+commentBO.getVlogId(),1);


        // 返回最新的评论
        CommentVO commentVO = new CommentVO();

        BeanUtils.copyProperties(comment,commentVO);

        return commentVO;
    }




}
