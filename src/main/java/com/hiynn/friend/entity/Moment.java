package com.hiynn.friend.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xuxitan
 * @description
 * @date 2020/2/13 16:19
 **/
@Data
public class Moment {
    private String id;
    private String userId;
    private String content;
    private String image;
    private String topic;
    private int isShow;
    private int isOwn;
    private int moments;
    private int praises;
    private List<String> praiseList;
    private List<Replay> replay;
    private Date createTime;
}
