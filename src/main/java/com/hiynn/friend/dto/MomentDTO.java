package com.hiynn.friend.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author xuxitan
 * @description
 * @date 2020/2/14 14:55
 **/
@Data
@ApiModel(value = "发布朋友圈实体", description = "发布朋友圈实体")
public class MomentDTO {

    @ApiModelProperty(value = "发布者id", required = true)
    private String userId;

    @ApiModelProperty(value = "发布者内容", required = true)
    private String content;

    @ApiModelProperty(value = "话题(非必填)")
    private String topic;

    @ApiModelProperty(value = "是否公开,默认公开 1公开 0设置隐私",required = true)
    private int isShow;

    @ApiModelProperty(value = "配图(非必传)")
    private MultipartFile file;
}
