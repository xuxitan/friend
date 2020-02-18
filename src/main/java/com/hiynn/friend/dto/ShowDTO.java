package com.hiynn.friend.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xuxitan
 * @description
 * @date 2020/2/18 15:53
 **/
@Data
@ApiModel(value = "设置公开、隐私请求实体",description = "设置公开、隐私请求实体")
public class ShowDTO {

    @ApiModelProperty(value = "用户id",required = true)
    private String userId;


    @ApiModelProperty(value = "朋友圈id",required = true)
    private String momentId;


    @ApiModelProperty(value = "是否公开,默认公开 1公开 0设置隐私",required = true)
    private int isShow;
}
