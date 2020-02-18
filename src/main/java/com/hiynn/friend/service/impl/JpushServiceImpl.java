package com.hiynn.friend.service.impl;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.alibaba.fastjson.JSON;
import com.hiynn.friend.config.JpushConfig;
import com.hiynn.friend.dto.JPushReqParam;
import com.hiynn.friend.service.JpushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xuxitan
 * @description
 * @date 2019/11/27 11:17
 **/
@Service
@Slf4j
public class JpushServiceImpl implements JpushService {

    @Autowired
    private JpushConfig jpushConfig;

    /***
     * 描述 给群组发送消息，支持多个群
     * 群组id对应极光的tag
     * @author xuxitan
     * @date 2019/11/27 15:47
     * @param reqParam
     * @return cn.jpush.api.push.PushResult
     */
    @Override
    public PushResult sendGroupMessage(JPushReqParam reqParam) {
        log.info("极光给群组推送入参【{}】", JSON.toJSON(reqParam));

        //构建极光client
        JPushClient jpushClient = buildJPushClient();

        //根据标签(tag)构建PushPayload
        PushPayload payload = buildPushPayLoadByTag(reqParam);

        //推送消息
        return send(jpushClient, payload);
    }


    /***
     * 描述 给指定人发送消息，支持多人
     * personId对应极光的alia
     * @author xuxitan
     * @date 2019/11/27 14:24
     * @param reqParam
     * @return void
     */
    @Override
    public PushResult sendPersonMessage(JPushReqParam reqParam) {

        log.info("极光给指定人员推送入参【{}】", JSON.toJSON(reqParam));

        //构建极光client
        JPushClient jpushClient = buildJPushClient();

        //根据alia构建PushPayload
        PushPayload payload = buildPushPayLoadByAlia(reqParam);

        //推送消息
        return send(jpushClient, payload);
    }

    /**
     * 根据别名(alia)等参数组装PushPayload
     * android和ios平台
     *
     * @param reqParam
     * @return
     */
    private PushPayload buildPushPayLoadByAlia(JPushReqParam reqParam) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.alias(reqParam.getPersonIds()))
                .setNotification(Notification.newBuilder().setAlert(reqParam.getContent())
                        .addPlatformNotification(
                                AndroidNotification.newBuilder().setTitle(reqParam.getTitle()).addExtras(reqParam.getExtrasMap()).build())
                        .addPlatformNotification(IosNotification.newBuilder().incrBadge(1).addExtras(reqParam.getExtrasMap()).build())
                        .build())
                .build();
    }

    /**
     * 根据标签(tag)等参数组装PushPayload
     * android和ios平台
     *
     * @param reqParam
     * @return
     */
    private PushPayload buildPushPayLoadByTag(JPushReqParam reqParam) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.tag(reqParam.getGroupIds()))
                .setNotification(Notification.newBuilder().setAlert(reqParam.getContent())
                        .addPlatformNotification(
                                AndroidNotification.newBuilder().setTitle(reqParam.getTitle()).addExtras(reqParam.getExtrasMap()).build())
                        .addPlatformNotification(IosNotification.newBuilder().incrBadge(1).addExtras(reqParam.getExtrasMap()).build())
                        .build())
                .build();
    }


    /**
     * 构建极光推送client
     *
     * @return
     */
    private JPushClient buildJPushClient() {
        ClientConfig clientConfig = ClientConfig.getInstance();
        clientConfig.setTimeToLive(Long.valueOf(jpushConfig.getLiveTime()));
        // 使用NativeHttpClient网络客户端，连接网络的方式，不提供回调函数
        JPushClient jpushClient = new JPushClient(jpushConfig.getMasterSecret(), jpushConfig.getAppkey(), null,
                clientConfig);
        return jpushClient;
    }

    /**
     * 调用极光推送
     *
     * @param jpushClient
     * @param payload
     * @return
     */
    private PushResult send(JPushClient jpushClient, PushPayload payload) {
        //极光返回结果
        PushResult result = null;
        try {
            result = jpushClient.sendPush(payload);
            log.info("极光推送结果【{}】 ", JSON.toJSON(result));
        } catch (APIConnectionException e) {
            log.error("极光推送连接错误，请稍后重试 ", e);
            log.error("Sendno:【{}】", payload.getSendno());
        } catch (APIRequestException e) {
            log.error("极光服务器响应出错，请修复！ ", e);
            log.info("HTTP Status: " + e.getStatus());
            log.info("Error Code: " + e.getErrorCode());
            log.info("Error Message: " + e.getErrorMessage());
            log.info("Msg ID: " + e.getMsgId());
            log.error("Sendno: " + payload.getSendno());
        } finally {
            jpushClient.close();
        }
        return result;
    }
}
