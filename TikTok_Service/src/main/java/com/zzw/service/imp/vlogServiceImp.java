package com.zzw.service.imp;

import com.github.pagehelper.PageHelper;
import com.tencentcloudapi.cbs.v20170312.models.Price;
import com.zzw.base.BaseInfoProperties;
import com.zzw.bo.VlogBO;
import com.zzw.enums.YesOrNo;
import com.zzw.mapper.MyLikedVlogMapper;
import com.zzw.mapper.VlogMapper;
import com.zzw.mapper.VlogMapperDIY;
import com.zzw.pojo.MyLikedVlog;
import com.zzw.pojo.Vlog;
import com.zzw.service.fansService;
import com.zzw.service.vlogService;
import com.zzw.utils.PagedGridResult;
import com.zzw.vo.IndexVlogVO;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service
public class vlogServiceImp extends BaseInfoProperties implements vlogService {

    @Autowired
    private VlogMapperDIY vlogMapperDIY;

    @Autowired
    private VlogMapper vlogMapper;

    @Autowired
    private MyLikedVlogMapper myLikedVlogMapper;

    @Autowired
    private com.zzw.service.fansService fansService;

    @Autowired
    private  Sid sid;

    @Transactional
    @Override
    public void creatVlog(VlogBO vlogBO) {
        String vlogId = sid.nextShort();
        Vlog newVlog = new Vlog();
        BeanUtils.copyProperties(vlogBO,newVlog);
        newVlog.setId(vlogId);
        newVlog.setLikeCounts(0);
        newVlog.setCommentsCounts(0);
        newVlog.setIsPrivate(YesOrNo.NO.type);
        newVlog.setCreatedTime(new Date());
        newVlog.setUpdatedTime(new Date());
        int resCode = vlogMapper.insert(newVlog);
    }

    @Override
    public PagedGridResult getIndexVlogList(String userId,String search,Integer page,Integer pageSize) {

        PageHelper.startPage(page,pageSize);

        Map<String,Object> map = new HashMap<>();

        if(StringUtils.isNotBlank(search)){
            map.put("search",search);
        }

        List<IndexVlogVO> indexVlogList = vlogMapperDIY.getIndexVlogList(map);

        for(IndexVlogVO vo:indexVlogList){

            String vlogId = vo.getVlogId();

            String vlogerId = vo.getVlogerId();
            //查看用户是否点赞了视频--每个页面都要显示
            if(StringUtils.isNotBlank(userId)){
                vo.setDoILikeThisVlog(doILikeThisVlog(userId,vlogId));
            }

            if(StringUtils.isNotBlank(vlogId)){
                vo.setLikeCounts(getVlogLikeCount(vlogId));
            }

        }

        PagedGridResult pagedGridResult = setterPagedGrid(indexVlogList, page);

        return pagedGridResult;
    }


    //去redis查询-用户是否点赞视频
    private boolean doILikeThisVlog(String userId,String vlogId){
        String userLikeVlog = redis.get(REDIS_USER_LIKE_VLOG + ":" + userId + ":" + vlogId);

        boolean doILike =false;

        if(StringUtils.isNotBlank(userLikeVlog)&&userLikeVlog.equalsIgnoreCase("1")){
                doILike=true;
        }
//
        return doILike;

    }

    //去redis查询-用户是否点赞视频
    public Integer getVlogLikeCount(String vlogId){
        String VlogLikeCount = redis.get(REDIS_VLOG_BE_LIKED_COUNTS + ":" + vlogId);

        if(StringUtils.isBlank(VlogLikeCount)){
            VlogLikeCount="0";
        }

        return Integer.parseInt(VlogLikeCount);

    }

    @Override
    public IndexVlogVO getDetailByVlogId(String vlogId) {

        Map<String,Object> map = new HashMap<>();

        map.put("vlogId",vlogId);

        List<IndexVlogVO> detailByVlogIdList = vlogMapperDIY.getDetailByVlogId(map);

        if(detailByVlogIdList.size()>0&&detailByVlogIdList!=null&&!detailByVlogIdList.isEmpty()){

            IndexVlogVO vlogVO = detailByVlogIdList.get(0);

            return vlogVO;

        }

        return null;
    }

    @Transactional
    @Override
    public void changeToPrivateOrPublic(String userId, String vlogId, Integer yesOrNo) {
        Example example = new Example(Vlog.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",vlogId);
        criteria.andEqualTo("vlogerId",userId);
        Vlog vlog = new Vlog();
        vlog.setIsPrivate(yesOrNo);
        vlogMapper.updateByExampleSelective(vlog,example);
    }

    @Override
    public PagedGridResult queryMyVlogList(String userId, Integer yesOrNo, Integer page, Integer pageSize) {
        Example example = new Example(Vlog.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("vlogerId",userId);
        criteria.andEqualTo("isPrivate",yesOrNo);
        PageHelper.startPage(page,pageSize);
        List<Vlog> vlogList = vlogMapper.selectByExample(example);
        PagedGridResult res = setterPagedGrid(vlogList, page);
        return res;
    }
    @Transactional
    @Override
    public void userLikedVlog(String userId, String vlogId) {
        MyLikedVlog myLikedVlog = new MyLikedVlog();
        String id = sid.nextShort();
        myLikedVlog.setId(id);
        myLikedVlog.setVlogId(vlogId);
        myLikedVlog.setUserId(userId);
        int resCode = myLikedVlogMapper.insert(myLikedVlog);
    }
    @Transactional
    @Override
    public void userUnlikeVlog(String userId, String vlogId) {
        MyLikedVlog myLikedVlog = new MyLikedVlog();
        myLikedVlog.setVlogId(vlogId);
        myLikedVlog.setUserId(userId);
        myLikedVlogMapper.delete(myLikedVlog);
    }


    @Override
    public PagedGridResult getMyLikedList(String userId, Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        Map<String,Object> map = new HashMap<>();
        map.put("userId",userId);
        List<IndexVlogVO> likedList = vlogMapperDIY.getMyLikedList(map);
        PagedGridResult res = setterPagedGrid(likedList, page);
        return res;
    }

    @Override
    public PagedGridResult getMyFollowList(String myId, Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        Map<String,Object> map = new HashMap<>();
        map.put("myId",myId);
        List<IndexVlogVO> myFollowList = vlogMapperDIY.getMyFollowList(map);
        for(IndexVlogVO vo:myFollowList){

            String vlogId = vo.getVlogId();

            String vlogerId = vo.getVlogerId();
            //查看用户是否点赞了视频--每个页面都要显示
            if(StringUtils.isNotBlank(myId)){
                //用户是否关注博主判断
                boolean doIFollow = fansService.queryDoIFollowVloger(myId, vlogerId);

                vo.setDoIFollowVloger(doIFollow);

                //用户是否点赞了视频
                vo.setDoILikeThisVlog(doILikeThisVlog(myId,vlogId));
            }

            if(StringUtils.isNotBlank(vlogId)){
                vo.setLikeCounts(getVlogLikeCount(vlogId));
            }

        }
        PagedGridResult res = setterPagedGrid(myFollowList, page);
        return res;
    }
}
