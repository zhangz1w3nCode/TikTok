package com.java.dao;

import com.java.pojo.Vlog;

public interface VlogMapper {
    int deleteByPrimaryKey(String id);

    int insert(Vlog record);

    int insertSelective(Vlog record);

    Vlog selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Vlog record);

    int updateByPrimaryKey(Vlog record);
}