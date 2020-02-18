package com.hiynn.friend.entity;

import lombok.Data;


/**
 * @author xuxitan
 * @description
 * @date 2020/2/13 16:02
 **/
@Data
public class TimeLine {

    private String id;

    private String userId;

    private String momentId;

    private Moment momentDetail;

    private int isShow;

    private int isOwn;

    private String createTime;
}
