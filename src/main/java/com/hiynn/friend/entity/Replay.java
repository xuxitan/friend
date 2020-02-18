package com.hiynn.friend.entity;

import lombok.Data;

/**
 * @author xuxitan
 * @description
 * @date 2020/2/13 16:20
 **/
@Data
public class Replay {
    private String from;

    private String fromImg;

    private String fromUserName;

    private String to;

    private String toUserName;

    private String content;

    private String time;
}
