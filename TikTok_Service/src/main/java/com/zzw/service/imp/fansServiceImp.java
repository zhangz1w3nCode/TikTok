package com.zzw.service.imp;

import com.github.pagehelper.PageHelper;
import com.zzw.base.BaseInfoProperties;
import com.zzw.bo.VlogBO;
import com.zzw.enums.YesOrNo;
import com.zzw.mapper.FansMapper;
import com.zzw.mapper.FansMapperDIY;
import com.zzw.mapper.VlogMapper;
import com.zzw.mapper.VlogMapperDIY;
import com.zzw.pojo.Fans;
import com.zzw.pojo.Vlog;
import com.zzw.service.fansService;
import com.zzw.service.vlogService;
import com.zzw.utils.PagedGridResult;
import com.zzw.vo.FansVO;
import com.zzw.vo.IndexVlogVO;
import com.zzw.vo.VlogerVO;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class fansServiceImp extends BaseInfoProperties implements fansService {

    @Autowired
    private FansMapper fansMapper;

    @Autowired
    private FansMapperDIY fansMapperDIY;


    @Autowired
    private  Sid sid;


    @Transactional
    @Override
    public void creatFans(String myId, String vlogerId) {//我 关注 别人  -- 我是粉丝
        String Id = sid.nextShort();
        Fans fans = new Fans();

        fans.setId(Id);

        fans.setVlogerId(vlogerId);

        fans.setFanId(myId);
            //看我的粉丝列表
        Fans myFans = queryFansRelationship(myId,vlogerId);

        if(myFans!=null){
              fans.setIsFanFriendOfMine(YesOrNo.YES.type);
            myFans.setIsFanFriendOfMine(YesOrNo.YES.type);
            fansMapper.updateByPrimaryKeySelective(myFans);
          }else{
            fans.setIsFanFriendOfMine(YesOrNo.NO.type);
          }
        fansMapper.insert(fans);
    }



    @Transactional
    @Override
    public void cancelFollow(String myId, String vlogerId) {

        //查看博主 和 我 的关系 --
        Fans vloger_fans = queryFansRelationship(vlogerId,myId);


        //如果存在关系 并且 关系为 1 -也就是互相关注
        if(vloger_fans!=null&&vloger_fans.getIsFanFriendOfMine()==YesOrNo.YES.type){


            //查看我和博主的关系 -- 我的粉丝
            Fans fans_vlog = queryFansRelationship(myId,vlogerId);

            //我对博主的关系-设置为 不关注 0
            fans_vlog.setIsFanFriendOfMine(YesOrNo.NO.type);

            //更新到数据库
            fansMapper.updateByPrimaryKeySelective(fans_vlog);
        }

        fansMapper.delete(vloger_fans);
    }

    //去查看我的粉丝列表里面有没有 对方
    @Override
    public Fans queryFansRelationship(String vlogerId, String fansId) {
        Example example = new Example(Fans.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("vlogerId",vlogerId);
        criteria.andEqualTo("fanId",fansId);
        //以 博主id 和 粉丝 id 作为条件 看看 对应的互粉状态
        List<Fans> fansList = fansMapper.selectByExample(example);
        Fans fan = null;
        if(fansList!=null&&fansList.size()>0&&!fansList.isEmpty()){
            fan = fansList.get(0);
        }
        return fan;
    }


    //查询 我 是否 关注了博主
    @Override
    public boolean queryDoIFollowVloger(String myId, String vlogerId) {

        Fans fans = queryFansRelationship(vlogerId, myId);

        return fans!=null;
    }

    @Override
    public PagedGridResult queryMyVlogList(String myId, Integer page, Integer pageSize) {

        Map<String,Object> map = new HashMap<>();
        map.put("myId",myId);

        PageHelper.startPage(page,pageSize);

        List<VlogerVO> myFollowList = fansMapperDIY.getMyFollowList(map);

        PagedGridResult res = setterPagedGrid(myFollowList, page);

        return res;
    }

    @Override
    public PagedGridResult getMyFansList(String myId, Integer page, Integer pageSize) {

        Map<String,Object> map = new HashMap<>();
        map.put("myId",myId);

        PageHelper.startPage(page,pageSize);

        List<FansVO> myFansList = fansMapperDIY.getMyFansList(map);

        //互粉的实现

        for (FansVO f:myFansList) {

            //查看 我(当前用户) 和 每个粉丝的关系 去redis查
            String relationship = redis.get(REDIS_FANS_AND_VLOGGER_RELATIONSHIP + ":" + myId + ":" + f.getFanId());

            //如果有关系的话则 设置属性为 true
            if(StringUtils.isNotBlank(relationship)&&relationship.equalsIgnoreCase("1")){
                f.setFriend(true);
            }

        }


        PagedGridResult res = setterPagedGrid(myFansList, page);

        return res;
    }

}
