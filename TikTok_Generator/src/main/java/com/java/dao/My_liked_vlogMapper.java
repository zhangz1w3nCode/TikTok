package com.java.dao;

import com.java.pojo.My_liked_vlog;

public interface My_liked_vlogMapper {
    int deleteByPrimaryKey(String id);

    int insert(My_liked_vlog record);

    int insertSelective(My_liked_vlog record);

    My_liked_vlog selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(My_liked_vlog record);

    int updateByPrimaryKey(My_liked_vlog record);
}