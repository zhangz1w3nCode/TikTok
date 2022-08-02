package com.zzw.mapper;

import com.zzw.vo.FansVO;
import com.zzw.vo.IndexVlogVO;
import com.zzw.vo.VlogerVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface FansMapperDIY {

    public List<VlogerVO> getMyFollowList(@Param("paramMap")Map<String,Object> map);
    public List<FansVO> getMyFansList(@Param("paramMap")Map<String,Object> map);

}