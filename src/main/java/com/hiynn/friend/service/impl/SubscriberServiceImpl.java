package com.hiynn.friend.service.impl;

import com.hiynn.friend.dto.SubscriberDtO;
import com.hiynn.friend.entity.Subscriber;
import com.hiynn.friend.service.SubscriberService;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author xuxitan
 * @description
 * @date 2020/2/14 16:07
 **/
@Service
@Slf4j
public class SubscriberServiceImpl implements SubscriberService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /***
     * 描述 删除好友
     * @author xuxitan
     * @date 2020/2/14 16:31
     * @param subscriber
     * @return void
     */
    @Override
    public void removeSubscriber(SubscriberDtO subscriber) {
        Query query = new Query(Criteria.where("userId").is(subscriber.getUserId()));
        Update update = new Update().pull("subscriberList", subscriber.getSubscriberId());
        mongoTemplate.updateFirst(query, update, "subscriber");
    }

    /***
     * 描述 添加好友
     * @author xuxitan
     * @date 2020/2/14 16:09
     * @param subscriber
     * @return void
     */
    @Override
    public void addSubscriber(SubscriberDtO subscriber) {
        //先查询是否有好友
        Query query = new Query(Criteria.where("userId").is(subscriber.getUserId()));
        Subscriber data = mongoTemplate.findOne(query, Subscriber.class, "subscriber");
        //如果已经有好友，则继续push好友
        if (null != data) {
            Update update = new Update().push("subscriberList", subscriber.getSubscriberId());
            UpdateResult result = mongoTemplate.updateFirst(query, update, "subscriber");
            //todo 将好友数量更新到用户信息缓存中
            log.info("添加好友成功:{},当前好友数量：{}", subscriber.getSubscriberId(), result.getMatchedCount());
            return;
        }
        Subscriber entity = new Subscriber();
        entity.setId(UUID.randomUUID().toString().replace("-", ""));
        entity.setUserId(subscriber.getUserId());
        List<String> list = new ArrayList<>();
        list.add(subscriber.getSubscriberId());
        mongoTemplate.save(entity, "subscriber");
        //todo 将好友数量更新到用户信息缓存中
        log.info("添加第一位好友成功:{}", subscriber.getSubscriberId());

    }
}
