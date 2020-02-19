package com.hiynn.friend.controller;

import com.hiynn.friend.dto.LoginDTO;
import com.hiynn.friend.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xuxitan
 * @description
 * @date 2020/2/17 16:23
 **/
@Api(tags = "系统登录")
@RestController
public class LoginController {


    @Autowired
    private LoginService loginService;

    /***
     * 描述 用户登录
     * @author xuxitan
     * @date 2020/2/17 16:29
     * @param login
     * @return java.lang.String
     */
    @ApiOperation(value = "登录", notes = "登录")
    @GetMapping("login")
    public String login(@RequestBody LoginDTO login){
        loginService.login(login);
        return "ok";
    }
}
