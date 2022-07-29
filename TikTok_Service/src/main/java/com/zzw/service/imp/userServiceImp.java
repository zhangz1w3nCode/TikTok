package com.zzw.service.imp;

import com.zzw.ao.UsersVO;
import com.zzw.bo.UpdateUserBO;
import com.zzw.enums.Sex;
import com.zzw.enums.UserInfoModifyType;
import com.zzw.enums.YesOrNo;
import com.zzw.grace.exceptions.GraceException;
import com.zzw.grace.result.ResponseStatusEnum;
import com.zzw.mapper.UsersMapper;
import com.zzw.pojo.Users;
import com.zzw.service.userService;
import com.zzw.utils.DateUtil;
import com.zzw.utils.DesensitizationUtil;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

@Service
public class userServiceImp implements userService {

    @Autowired
    public UsersMapper usersMapper;

    @Autowired
    public Sid sid;

    private static final String USER_FACE1= "http://122.152.205.72:88/group1/M00/00/05/CpoxxF6ZUySASMbOAABBAXhjY0Y649.png";

    @Override
    public Users queryUserIsExit(String mobile) {
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("mobile",mobile);
        Users users = usersMapper.selectOneByExample(example);
        return users;
    }


    @Transactional
    @Override
    public Users creatUserIs(String mobile) {

        String userId = sid.nextShort();

        Users user = new Users();

        user.setId(userId);
        user.setMobile(mobile);
        //脱敏工具
        user.setNickname("用户：" + DesensitizationUtil.commonDisplay(mobile));
        user.setImoocNum("用户：" + DesensitizationUtil.commonDisplay(mobile));
        user.setFace(USER_FACE1);
        user.setBirthday(DateUtil.stringToDate("1900-01-01"));
        user.setSex(Sex.secret.type);
        user.setCountry("中国");
        user.setProvince("");
        user.setCity("");
        user.setDistrict("");
        user.setDescription("这家伙很懒,什么都没留下~");
        user.setCanImoocNumBeUpdated(YesOrNo.YES.type);
        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());

        usersMapper.insert(user);

        return user;
    }

    @Override
    public Users queryUserInfo(String userId) {
        Users users = usersMapper.selectByPrimaryKey(userId);
        return users;
    }

    @Transactional
    @Override
    public Users updateUserInfo(UpdateUserBO UpdateUserBO) {
        Users users = new Users();
        BeanUtils.copyProperties(UpdateUserBO,users);


        int resCode = usersMapper.updateByPrimaryKeySelective(users);

        if(resCode!=1){
            GraceException.display(ResponseStatusEnum.USER_UPDATE_ERROR);
        }

        return queryUserInfo(UpdateUserBO.getId());
    }

    @Override
    public Users updateUserInfo(UpdateUserBO UpdateUserBO, Integer type) {

        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();

        if(type== UserInfoModifyType.NICKNAME.type){//修改昵称

            criteria.andEqualTo("nickname",UpdateUserBO.getNickname());
            Users user = usersMapper.selectOneByExample(example);
            if(user!=null){ //要修改的信息 如果数据库有则 修改失败
                GraceException.display(ResponseStatusEnum.USER_INFO_UPDATED_NICKNAME_EXIST_ERROR);
            }
        }

        if(type== UserInfoModifyType.IMOOCNUM.type){//修改TikTok号

            criteria.andEqualTo("imoocNum",UpdateUserBO.getImoocNum());
            Users user = usersMapper.selectOneByExample(example);
            if(user!=null){ //要修改的信息 如果数据库有则 修改失败
                GraceException.display(ResponseStatusEnum.USER_INFO_UPDATED_IMOOCNUM_EXIST_ERROR);
            }

            //查看tiktok号能否被修改 只能修改一次
            Users tempUser = queryUserInfo(UpdateUserBO.getId());

            if(tempUser.getCanImoocNumBeUpdated()==YesOrNo.NO.type){//tiktok号能否修改的值为 0 说明不能修改了
                GraceException.display(ResponseStatusEnum.USER_INFO_CANT_UPDATED_IMOOCNUM_ERROR);
            }
            //能被修改则 修改完后 设置为不能修改 也就是0
            UpdateUserBO.setCanImoocNumBeUpdated(YesOrNo.NO.type);
        }

        return updateUserInfo(UpdateUserBO);
    }
}
