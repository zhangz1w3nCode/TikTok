package com.zzw.service.imp;

import com.github.pagehelper.PageHelper;
import com.zzw.base.BaseInfoProperties;
import com.zzw.bo.CommentBO;
import com.zzw.enums.MessageEnum;
import com.zzw.enums.YesOrNo;
import com.zzw.mapper.CommentMapper;
import com.zzw.mo.messageMO;
import com.zzw.pojo.Comment;
import com.zzw.pojo.Users;
import com.zzw.pojo.Vlog;
import com.zzw.rabbitmqConfig;
import com.zzw.service.commentService;
import com.zzw.utils.JsonUtils;
import com.zzw.utils.PagedGridResult;
import com.zzw.vo.CommentVO;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class commentServiceImp extends BaseInfoProperties implements commentService {

    @Autowired
    private CommentMapper CommentMapper;

    @Autowired
    private com.zzw.mapper.commentMapperDIY commentMapperDIY;

    @Autowired
    private com.zzw.mapper.VlogMapper vlogMapper;

    @Autowired
    private com.zzw.mapper.UsersMapper UsersMapper;

    @Autowired
    private com.zzw.service.msgService msgService;
    @Autowired
    private  Sid sid;

    @Autowired
    public RabbitTemplate rabbitTemplate;

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



        Vlog vlog = vlogMapper.selectByPrimaryKey(commentBO.getVlogId());

        String cover = vlog.getCover();

        String commentContent = commentBO.getContent();

        Map<String,String> map = new HashMap();

        map.put("vlogId",vlog.getId());
        map.put("vlogCover",cover);
        map.put("commentId",Id);
        map.put("commentContent",commentContent);

        int type=MessageEnum.COMMENT_VLOG.type;
        String typeStr=MessageEnum.COMMENT_VLOG.value;

        if(StringUtils.isNotBlank(commentBO.getFatherCommentId())&&!commentBO.getFatherCommentId().equalsIgnoreCase("0")){
            type = MessageEnum.REPLY_YOU.type;
            typeStr = MessageEnum.REPLY_YOU.value;
        }

        //回复完对方后-要把回复信息 发送给对方
        //msgService.creatMsg(commentBO.getCommentUserId(),commentBO.getVlogerId(),type,map);

        messageMO msg = new messageMO();
        msg.setFromUserId(commentBO.getCommentUserId());
        msg.setToUserId(commentBO.getVlogerId());
        msg.setMsgContent(map);

        //消息对象转换成字符串
        String msgstr = JsonUtils.objectToJson(msg);

        rabbitTemplate.convertAndSend(rabbitmqConfig.EXCHANGE_MSG
                ,"system.msg."+typeStr
                ,msgstr);



        return commentVO;
    }

    @Override
    public PagedGridResult getCommentList(String userId,String vlogId, Integer page, Integer pageSize) {
        Map<String,Object> map = new HashMap<>();
        map.put("vlogId",vlogId);

        PageHelper.startPage(page,pageSize);

        List<CommentVO> commentList = commentMapperDIY.getCommentList(map);

        for(CommentVO cv :commentList){


            //获取评论总数
            String commentCount = redis.get(REDIS_VLOG_COMMENT_LIKED_COUNTS + ":" + cv.getCommentId());
            int commentNum=0;

            if(StringUtils.isNotBlank(commentCount)){
                 commentNum = Integer.parseInt(commentCount);
            }

            cv.setLikeCounts(commentNum);

            //用户是否对视频点赞
            String doILikeComment = redis.get(REDIS_USER_LIKE_COMMENT + ":" + userId + ":" + cv.getCommentId());

            if(StringUtils.isNotBlank(doILikeComment)&&doILikeComment.equalsIgnoreCase("1")){

                cv.setIsLike(YesOrNo.YES.type);

            }

        }

        PagedGridResult res = setterPagedGrid(commentList, page);

        return res;
    }

    @Override
    public void deleteComment(String commentUserId, String commentId, String vlogId) {

        Comment comment = new Comment();
        comment.setCommentUserId(commentUserId);
        comment.setId(commentId);


        //删除数据库--删除缓存
        int code = CommentMapper.delete(comment);

        //点赞数量-1
        redis.decrement(REDIS_VLOG_COMMENT_COUNTS+":"+vlogId,1);
    }

    @Override
    public void addToMongoDB(String commentId, String userId) {

        Comment comment = CommentMapper.selectByPrimaryKey(commentId);

        Vlog vlog = vlogMapper.selectByPrimaryKey(comment.getVlogId());

        Users users = UsersMapper.selectByPrimaryKey(userId);

        String cover = vlog.getCover();

        String commentContent = comment.getContent();

        Map<String,String> map = new HashMap();

        map.put("vlogId",vlog.getId());
        map.put("vlogCover",cover);
        map.put("commentId",commentId);
        map.put("commentContent",commentContent);


        messageMO msg = new messageMO();
        msg.setFromUserId(comment.getCommentUserId());
        msg.setToUserId(comment.getVlogerId());
        msg.setMsgContent(map);

        //消息对象转换成字符串
        String msgstr = JsonUtils.objectToJson(msg);

        rabbitTemplate.convertAndSend(rabbitmqConfig.EXCHANGE_MSG
                ,"system.msg."+MessageEnum.LIKE_COMMENT.value
                ,msgstr);
    }
}
