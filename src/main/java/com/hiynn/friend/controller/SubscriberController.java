package com.hiynn.friend.controller;

import com.hiynn.friend.dto.SubscriberDtO;
import com.hiynn.friend.service.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 好友关注
 *
 * @author xuxitan
 * @description
 * @date 2020/2/14 16:02
 **/
@RestController
public class SubscriberController {

    @Autowired
    private SubscriberService subscriberService;

    /***
     * 描述 添加好友
     * @author xuxitan
     * @date 2020/2/14 16:05
     * @param subscriber
     * @return java.lang.String
     */
    @PostMapping("subscriber")
    public String add(@RequestBody SubscriberDtO subscriber) {
        subscriberService.addSubscriber(subscriber);
        return "ok";
    }

    /***
     * 描述 删除好友
     * @author xuxitan
     * @date 2020/2/17 11:03
     * @param subscriber
     * @return java.lang.String
     */
    @DeleteMapping("subscriber")
    public String remove(@RequestBody SubscriberDtO subscriber){
        subscriberService.removeSubscriber(subscriber);
        return "ok";
    }
}
