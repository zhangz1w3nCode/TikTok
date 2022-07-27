package com.java.dao;

import com.java.pojo.Fans;

public interface FansMapper {
    int deleteByPrimaryKey(String id);

    int insert(Fans record);

    int insertSelective(Fans record);

    Fans selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Fans record);

    int updateByPrimaryKey(Fans record);
}