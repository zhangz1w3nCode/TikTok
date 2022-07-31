package com.zzw.controller;

import com.zzw.base.BaseInfoProperties;
import com.zzw.bo.RegisterLoginBO;
import com.zzw.bo.VlogBO;
import com.zzw.grace.result.GraceJSONResult;
import com.zzw.grace.result.ResponseStatusEnum;
import com.zzw.pojo.Users;
import com.zzw.service.vlogService;
import com.zzw.utils.IPUtil;
import com.zzw.utils.PagedGridResult;
import com.zzw.utils.SMSUtils;
import com.zzw.vo.IndexVlogVO;
import com.zzw.vo.UsersVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Api(tags = "视频接口")
@RequestMapping("vlog")
@RestController
@CrossOrigin
@Slf4j
public class vlogController extends BaseInfoProperties {

    @Autowired
    private com.zzw.service.vlogService vlogService;

    @Autowired
    private com.zzw.service.imp.userServiceImp userServiceImp;

    //上传视频成功后-保存视频信息到数据库
    @PostMapping("publish")
    public GraceJSONResult publish(@RequestBody VlogBO vlogBO){

        //Fixme:校验 vlogbo的参数
        vlogService.creatVlog(vlogBO);

        return GraceJSONResult.ok("上传视频成功");
    }



    //获取视频信息列表 通过关键字进行模糊搜索-
    //todo 可以使用es去优化 因为 这里用了 'like %xxxx%' 无法用索引;

    @GetMapping("indexList")
    public GraceJSONResult indexList(@RequestParam(defaultValue = "") String search,
                                     @RequestParam Integer page,
                                     @RequestParam Integer pageSize){

        if(page==null) page = COMMON_START_PAGE;

        if(pageSize ==null) pageSize  =COMMON_PAGE_SIZE;


        PagedGridResult pagedGridResult = vlogService.getIndexVlogList(search, page, pageSize);

        return GraceJSONResult.ok(pagedGridResult);
    }

}
