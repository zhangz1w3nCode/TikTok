package com.zzw.mapper;

import com.zzw.my.mapper.MyMapper;
import com.zzw.pojo.Comment;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentMapper extends MyMapper<Comment> {
}