package com.zzw.service;

import com.zzw.bo.UpdateUserBO;
import com.zzw.bo.VlogBO;
import com.zzw.pojo.Users;
import com.zzw.utils.PagedGridResult;
import com.zzw.vo.IndexVlogVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface vlogService {

    //创建新的视频信息
    public void creatVlog(VlogBO vlogBO);


    //查询视频信息
    public PagedGridResult getIndexVlogList(String search, Integer page, Integer pageSize);


}
