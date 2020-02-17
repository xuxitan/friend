package com.hiynn.friend.entity;

import lombok.Data;

import java.util.List;

/**
 * @author xuxitan
 * @description
 * @date 2020/2/14 15:18
 **/
@Data
public class Subscriber {

    private String id;

    private String userId;

    private List<String> subscriberList;
}
