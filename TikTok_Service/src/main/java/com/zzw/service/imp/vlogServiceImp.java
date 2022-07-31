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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
}
