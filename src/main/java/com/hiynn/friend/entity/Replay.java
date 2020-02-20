package com.hiynn.friend.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xuxitan
 * @description
 * @date 2020/2/13 16:20
 **/
@Data
public class Replay implements Serializable {

    private static final long serialVersionUID = -6218582321544432396L;

    private String from;

    private String fromImg;

    private String fromUserName;

    private String to;

    private String toUserName;

    private String content;

    private String time;
}
