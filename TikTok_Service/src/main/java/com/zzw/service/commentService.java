package com.zzw.service;

import com.zzw.bo.CommentBO;
import com.zzw.pojo.Fans;
import com.zzw.utils.PagedGridResult;
import com.zzw.vo.CommentVO;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public interface commentService {

    //创建一个评论
    public CommentVO creatComment(CommentBO commentBO);

    //获取评论
    public PagedGridResult getCommentList(String userId,String vlogId,Integer page, Integer pageSize);

    void deleteComment(String commentUserId, String commentId, String vlogId);

    void addToMongoDB(String commentId,String userId);

}
