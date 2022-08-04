package com.zzw.service;

import com.zzw.mo.messageMO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface msgService {

    //创建新的视频信息
    public void creatMsg(String fromUserId, String toUserId, Integer msgType, Map msgContent);

    List<messageMO> getCommentList(String userId, Integer page, Integer pageSize);
}
