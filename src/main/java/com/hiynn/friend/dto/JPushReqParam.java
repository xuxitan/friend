package com.hiynn.friend.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author xuxitan
 * @description 极光推送请求参数实体
 * @date 2019/11/27 13:15
 **/
@Data
public class JPushReqParam implements Serializable {

    private static final long serialVersionUID = -8540429448535272543L;

    /**
     * App通知栏标题
     */
    private String title;

    /**
     * App通知栏内容（为了单行显示全，尽量保持在22个汉字以下）
     */
    private String content;

    /**
     * 群组id集合，发送群组消息必填
     */
    private String[] groupIds;

    /**
     * 人员id集合，发送消息给指定id人员必填
     */
    private String[] personIds;

    /**
     * 额外推送信息（不会显示在通知栏，传递数据用）
     */
    private Map<String, String> extrasMap;

}
