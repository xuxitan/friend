package com.hiynn.friend.service;

import cn.jpush.api.push.PushResult;
import com.hiynn.friend.dto.JPushReqParam;

/***
 * 描述 极光推送接口
 * @author xuxitan
 * @date 2019/11/27 11:16
 */
public interface JpushService {

    /***
     * 描述 给指定人发送消息，支持多人
     * @author xuxitan
     * @date 2019/11/27 15:46
     * @param reqParam
     * @return cn.jpush.api.push.PushResult
     */
    PushResult sendPersonMessage(JPushReqParam reqParam);


    /***
     * 描述 给群组发送消息，支持多个群
     * @author xuxitan
     * @date 2019/11/27 15:47
     * @param reqParam
     * @return cn.jpush.api.push.PushResult
     */
    PushResult sendGroupMessage(JPushReqParam reqParam);


}
