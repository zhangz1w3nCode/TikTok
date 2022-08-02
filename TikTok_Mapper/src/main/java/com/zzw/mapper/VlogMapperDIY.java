package com.zzw.mapper;

import com.zzw.my.mapper.MyMapper;
import com.zzw.pojo.Vlog;
import com.zzw.vo.IndexVlogVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface VlogMapperDIY{
    public List<IndexVlogVO> getIndexVlogList(@Param("paramMap")Map<String,Object> map);
    public List<IndexVlogVO> getDetailByVlogId(@Param("paramMap")Map<String,Object> map);
    List<IndexVlogVO> getMyLikedList(@Param("paramMap")Map<String,Object> map);
    List<IndexVlogVO> getMyFollowList(@Param("paramMap")Map<String,Object> map);
    List<IndexVlogVO> getMyFriendList(@Param("paramMap")Map<String,Object> map);
}