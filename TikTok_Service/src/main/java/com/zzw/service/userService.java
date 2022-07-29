package com.zzw.service;

import com.zzw.bo.UpdateUserBO;
import com.zzw.pojo.Users;
import org.springframework.stereotype.Service;

@Service
public interface userService {

    public Users queryUserIsExit(String mobile);

    public Users creatUserIs(String mobile);

    public Users queryUserInfo(String userId);

    public Users updateUserInfo(UpdateUserBO UpdateUserBO);

    public Users updateUserInfo(UpdateUserBO UpdateUserBO,Integer type);




}
