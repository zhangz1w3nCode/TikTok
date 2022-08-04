package com.zzw.service.imp;

import com.github.pagehelper.PageHelper;
import com.zzw.base.BaseInfoProperties;
import com.zzw.enums.MessageEnum;
import com.zzw.enums.YesOrNo;
import com.zzw.mapper.FansMapper;
import com.zzw.mapper.FansMapperDIY;
import com.zzw.mapper.UsersMapper;
import com.zzw.mo.messageMO;
import com.zzw.pojo.Fans;
import com.zzw.pojo.Users;
import com.zzw.repository.messageRepository;
import com.zzw.service.fansService;
import com.zzw.service.msgService;
import com.zzw.service.userService;
import com.zzw.utils.PagedGridResult;
import com.zzw.vo.FansVO;
import com.zzw.vo.VlogerVO;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class msgServiceImp extends BaseInfoProperties implements msgService {

    @Autowired
    private com.zzw.repository.messageRepository messageRepository;

    @Autowired
    private com.zzw.service.userService userService;


    //创建消息
    @Override
    public void creatMsg(String fromUserId, String toUserId, Integer msgType, Map msgContent) {//我 关注 别人  -- 我是粉丝
        messageMO messageMO = new messageMO();
        Users user = userService.queryUserInfo(fromUserId);

        messageMO.setFromUserId(fromUserId);
        messageMO.setFromNickname(user.getNickname());
        messageMO.setFromFace(user.getFace());

        if(msgContent!=null){
            messageMO.setMsgContent(msgContent);
        }

        messageMO.setMsgType(msgType);
        messageMO.setToUserId(toUserId);
        messageMO.setCreateDate(new Date());


        messageRepository.save(messageMO);

    }


    @Override
    public List<messageMO> getCommentList(String userId, Integer page, Integer pageSize) {

        PageRequest pageRequest = PageRequest.of(page, pageSize);

        List<messageMO> list = messageRepository.findAllByToUserIdOrderByCreateDateDesc(userId, pageRequest);

        // 遍历我发送的每个消息
        for(messageMO mo:list){
            //查看消息类型 如果是关注类的消息 则说明我关注了对方
            // 则后续需要判断对方是否关注我「通过redis」
            // 来达互粉的目的
            if(mo.getMsgType()!=null&&mo.getMsgType()== MessageEnum.FOLLOW_YOU.type){

                Map map = mo.getMsgContent();
                if(map==null){
                    map = new HashMap();
                }
                //则后续需要判断对方是否关注我「通过redis」
                String relationship = redis.get(REDIS_FANS_AND_VLOGGER_RELATIONSHIP + ":" + mo.getToUserId() + ":" + mo.getFromUserId());
                //如果对方关注我则关系设置为互粉即 true
                if(StringUtils.isNotBlank(relationship)&&relationship.equalsIgnoreCase("1")){
                    map.put("isFriend",true);
                }else{
                    map.put("isFriend",false);
                }

                mo.setMsgContent(map);
            }


        }




        return list;
    }
}
