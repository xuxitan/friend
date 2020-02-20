package com.hiynn.friend.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author xuxitan
 * @description
 * @date 2020/2/14 15:18
 **/
@Data
public class Subscriber implements Serializable {

    private static final long serialVersionUID = 6085813934741124723L;

    private String id;

    private String userId;

    private List<UserBasic> subscriberList;
}
