package com.hiynn.friend.entity;

import lombok.Data;

import java.io.Serializable;


/**
 * @author xuxitan
 * @description
 * @date 2020/2/13 16:02
 **/
@Data
public class TimeLine implements Serializable {

    private static final long serialVersionUID = -2991118299136480355L;

    private String id;

    /**
     * 关注好友的用户id
     */
    private String userId;

    /**
     * 实际发送朋友圈的用户id
     */
    private String realityId;

    private String momentId;

    private Moment momentDetail;

    private int isShow;

    private int isOwn;

    private String createTime;
}
