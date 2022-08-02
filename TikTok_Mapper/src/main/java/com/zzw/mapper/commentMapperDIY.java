package com.zzw.mapper;

import com.zzw.vo.CommentVO;
import com.zzw.vo.IndexVlogVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface commentMapperDIY {
    public List<CommentVO> getCommentList(@Param("paramMap")Map<String,Object> map);

}