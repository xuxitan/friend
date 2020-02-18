package com.hiynn.friend.service;

import com.hiynn.friend.dto.LoginDTO;

/**
 * @author xuxitan
 * @description
 * @date 2020/2/17 16:30
 **/
public interface LoginService {

    /***
     * 描述 登录
     * @author xuxitan
     * @date 2020/2/17 16:30
     * @param login
     * @return void
     */
    void login(LoginDTO login);
}
