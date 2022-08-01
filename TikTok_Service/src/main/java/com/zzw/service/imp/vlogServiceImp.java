package com.zzw.service.imp;

import com.github.pagehelper.PageHelper;
import com.zzw.base.BaseInfoProperties;
import com.zzw.bo.VlogBO;
import com.zzw.enums.YesOrNo;
import com.zzw.mapper.VlogMapper;
import com.zzw.mapper.VlogMapperDIY;
import com.zzw.pojo.Vlog;
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
    public PagedGridResult getIndexVlogList(String search,Integer page,Integer pageSize) {

        PageHelper.startPage(page,pageSize);

        Map<String,Object> map = new HashMap<>();
        if(StringUtils.isNotBlank(search)){
            map.put("search",search);
        }

        List<IndexVlogVO> indexVlogList = vlogMapperDIY.getIndexVlogList(map);

        PagedGridResult pagedGridResult = setterPagedGrid(indexVlogList, page);

        return pagedGridResult;
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
}
