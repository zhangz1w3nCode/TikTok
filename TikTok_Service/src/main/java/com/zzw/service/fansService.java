package com.zzw.service;

import com.zzw.bo.VlogBO;
import com.zzw.pojo.Fans;
import com.zzw.utils.PagedGridResult;
import com.zzw.vo.IndexVlogVO;
import org.springframework.stereotype.Service;

@Service
public interface fansService {

    //关注-一个博主--成为他的粉丝
    public void creatFans(String myId, String vlogerId);

    public void cancelFollow(String myId, String vlogerId);

    public Fans queryFansRelationship(String vlogerId, String myId);

    public boolean queryDoIFollowVloger(String myId, String vlogerId);

    public PagedGridResult queryMyVlogList(String userId,Integer page, Integer pageSize);
    public PagedGridResult getMyFansList(String userId,Integer page, Integer pageSize);

}
