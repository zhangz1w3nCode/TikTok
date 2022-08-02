package com.zzw.service;

import com.zzw.bo.CommentBO;
import com.zzw.pojo.Fans;
import com.zzw.utils.PagedGridResult;
import com.zzw.vo.CommentVO;
import org.springframework.stereotype.Service;

@Service
public interface commentService {

    //创建一个评论
    public CommentVO creatComment(CommentBO commentBO);
}
