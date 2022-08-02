package com.zzw.service;

import com.zzw.bo.UpdateUserBO;
import com.zzw.bo.VlogBO;
import com.zzw.pojo.Users;
import com.zzw.utils.PagedGridResult;
import com.zzw.vo.IndexVlogVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface vlogService {

    //创建新的视频信息
    public void creatVlog(VlogBO vlogBO);

    //新增一个喜欢的视频
    public void userLikedVlog(String userId,String vlogId);

    //用户不喜欢一个视频
    public void userUnlikeVlog(String userId, String vlogId);

    //查询视频信息
    public PagedGridResult getIndexVlogList(String userId,String search, Integer page, Integer pageSize);

    public IndexVlogVO getDetailByVlogId(String vlogId);

    public void changeToPrivateOrPublic(String userId, String vlogId,Integer yesOrNo);

    public PagedGridResult queryMyVlogList(String userId,Integer yesOrNo,Integer page, Integer pageSize);

}
