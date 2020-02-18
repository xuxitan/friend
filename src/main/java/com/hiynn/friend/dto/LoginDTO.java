package com.hiynn.friend.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xuxitan
 * @description
 * @date 2020/2/17 16:25
 **/
@Data
@ApiModel(value = "用户登录实体", description = "用户登录实体")
public class LoginDTO {

    @ApiModelProperty(value = "用户名", required = true)
    private String userName;

    @ApiModelProperty(value = "用户密码", required = true)
    private String password;
}
