package com.hiynn.friend.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xuxitan
 * @description
 * @date 2020/2/17 17:23
 **/
@Data
public class UserBasic implements Serializable {

    private static final long serialVersionUID = -3761145103789790632L;

    private String userId;

    private String userName;

    private String img;

    private String createTime;
}
