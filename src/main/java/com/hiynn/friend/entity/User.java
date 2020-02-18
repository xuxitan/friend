package com.hiynn.friend.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author xuxitan
 * @description
 * @date 2020/2/17 16:37
 **/
@Data
public class User {

    private String id;

    private String userName;

    private String password;

    private String sign;

    private String img;

    private String phone;

    private String sex;

    private int age;

    private String email;

    /**
     * 0未激活 1激活
     */
    private int active;

    /**
     * 发布朋友圈的数量
     */
    private int moments;

    /**
     * 粉丝数量
     */
    private int fans;

    /**
     * 关注数量
     */
    private int subscribers;

    private String createTime;
}
