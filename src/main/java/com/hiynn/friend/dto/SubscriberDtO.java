package com.hiynn.friend.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xuxitan
 * @description
 * @date 2020/2/14 16:03
 **/
@Data
@ApiModel(value = "关注好友实体",description = "关注好友实体")
public class SubscriberDtO {

    @ApiModelProperty(value = "当前用户id",required = true)
    private String userId;

    @ApiModelProperty(value = "被关注的用户的id",required = true)
    private String subscriberId;
}
