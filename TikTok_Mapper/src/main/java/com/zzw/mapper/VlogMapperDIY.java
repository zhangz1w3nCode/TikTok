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
}