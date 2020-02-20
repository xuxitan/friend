package com.hiynn.friend.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author xuxitan
 * @description
 * @date 2020/2/13 16:19
 **/
@Data
public class Moment implements Serializable {

    private static final long serialVersionUID = 9024019218334585814L;

    private String id;
    private String userId;
    private String content;
    private String image;
    private String topic;
    private int isShow;
    private int isOwn;
    private int moments;
    private int praises;
    private List<UserBasic> praiseList;
    private List<Replay> replay;
    private String createTime;
}
