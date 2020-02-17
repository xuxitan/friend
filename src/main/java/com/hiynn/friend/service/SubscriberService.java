package com.hiynn.friend.service;

import com.hiynn.friend.dto.SubscriberDtO;

public interface SubscriberService {

    /***
     * 描述 添加好友
     * @author xuxitan
     * @date 2020/2/14 16:09
     * @param subscriber
     * @return void
     */
    void addSubscriber(SubscriberDtO subscriber);

    /***
     * 描述 删除好友
     * @author xuxitan
     * @date 2020/2/14 16:31
     * @param subscriber
     * @return void
     */
    void removeSubscriber(SubscriberDtO subscriber);
}
