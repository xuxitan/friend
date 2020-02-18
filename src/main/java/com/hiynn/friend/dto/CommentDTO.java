package com.hiynn.friend.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xuxitan
 * @description
 * @date 2020/2/17 10:43
 **/
@Data
@ApiModel(value = "评论、回复实体",description = "评论、回复实体")
public class CommentDTO {

    @ApiModelProperty(value = "朋友圈id",required = true)
    private String momentId;

    @ApiModelProperty(value = "评论者id",required = true)
    private String fromId;

    @ApiModelProperty(value = "被评论者id",required = true)
    private String toId;

    @ApiModelProperty(value = "评论、回复内容",required = true)
    private String content;
}
