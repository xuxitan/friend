package com.hiynn.friend.service.impl;

import com.hiynn.friend.dto.JPushReqParam;
import com.hiynn.friend.dto.SubscriberDtO;
import com.hiynn.friend.entity.Fan;
import com.hiynn.friend.entity.Subscriber;
import com.hiynn.friend.entity.User;
import com.hiynn.friend.entity.UserBasic;
import com.hiynn.friend.service.JpushService;
import com.hiynn.friend.service.SubscriberService;
import com.hiynn.friend.util.DateUtil;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
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
@Slf4j
@Service
public class SubscriberServiceImpl implements SubscriberService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private JpushService jpushService;

    /***
     * 描述 删除好友
     * @author xuxitan
     * @date 2020/2/14 16:31
     * @param subscriber
     * @return void
     */
    @Override
    public void removeSubscriber(SubscriberDtO subscriber) {
        Query query = new Query(Criteria.where("id").is(subscriber.getUserId()));
        User user = mongoTemplate.findOne(query, User.class);
        if (null == user) {
            throw new RuntimeException("用户不存在");
        }
        //关注数量减一
        user.setSubscribers(user.getSubscribers() > 1 ? user.getSubscribers() - 1 : 0);

        Query subQuery = new Query(Criteria.where("id").is(subscriber.getSubscriberId()));
        User subUser = mongoTemplate.findOne(subQuery, User.class);
        if (null == subUser) {
            throw new RuntimeException("用户不存在");
        }
        //粉丝数量减一
        subUser.setFans(subUser.getFans() > 1 ? subUser.getFans() - 1 : 0);

        //更新用户信息
        mongoTemplate.save(user,"user");
        mongoTemplate.save(subUser,"user");

        //关注列表删除
        Query sub = new Query(Criteria.where("userId").is(subscriber.getUserId()));
        Subscriber subList = mongoTemplate.findOne(sub, Subscriber.class);
        if (null == subList) {
            throw new RuntimeException("关注列表不存在");
        }

        //指定删除元素
        Query sQuery = new Query(Criteria.where("userId").is(subList.getUserId()));
        Document document = new Document();
        document.put("userId", subscriber.getSubscriberId());
        Update update = new Update().pull("subscriberList", document);
        mongoTemplate.updateFirst(sQuery, update, "subscriber");

        //粉丝列表删除
        Query fanQuery = new Query(Criteria.where("userId").is(subscriber.getSubscriberId()));
        Document fanDocument = new Document();
        document.put("userId", subscriber.getUserId());
        Update fanUpdate = new Update().pull("fanList", fanDocument);
        mongoTemplate.updateFirst(fanQuery, fanUpdate, "fan");

        //删除该好友时间线上的数据
        Query removeQuery = new Query();
        removeQuery.addCriteria(new Criteria().andOperator(Criteria.where("userId").is(subscriber.getUserId()), Criteria.where("realityId").is(subscriber.getSubscriberId())));
        mongoTemplate.remove(removeQuery, "subscriber");
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
        //关注通知
        JPushReqParam reqParam = new JPushReqParam();
        reqParam.setTitle("关注好友通知");
        reqParam.setContent("测试用户关注了您");
        reqParam.setPersonIds(new String[]{subscriber.getSubscriberId()});
        jpushService.sendPersonMessage(reqParam);

        //先查询是否有好友
        Query query = new Query(Criteria.where("userId").is(subscriber.getUserId()));
        Subscriber data = mongoTemplate.findOne(query, Subscriber.class, "subscriber");

        //查询被添加的用户信息
        Query fanQuery = new Query(Criteria.where("id").is(subscriber.getSubscriberId()));
        User fan = mongoTemplate.findOne(fanQuery, User.class);
        if (null == fan) {
            throw new RuntimeException("该用户不存在");
        }
        UserBasic userBasic = new UserBasic();
        userBasic.setUserId(subscriber.getSubscriberId());
        userBasic.setImg(fan.getImg());
        userBasic.setCreateTime(DateUtil.nowTime());
        userBasic.setUserName(fan.getUserName());
        //更新关注列表
        if (null != data) {
            data.getSubscriberList().add(userBasic);
        } else {
            data = new Subscriber();
            data.setId(UUID.randomUUID().toString().replace("-", ""));
            data.setUserId(subscriber.getUserId());
            List<UserBasic> subscriberList = new ArrayList<>();
            subscriberList.add(userBasic);
            data.setSubscriberList(subscriberList);
        }
        mongoTemplate.save(data, "subscriber");
        log.info("添加关注成功,用户名:{},", fan.getUserName());

        //更新自己的关注数量
        Query userQuery = new Query(Criteria.where("id").is(subscriber.getUserId()));
        User user = mongoTemplate.findOne(userQuery, User.class);
        if (null != user) {
            user.setSubscribers(user.getSubscribers() + 1);
        }
        mongoTemplate.save(user);

        //被关注的粉丝数量
        fan.setFans(fan.getFans() + 1);
        mongoTemplate.save(fan);

        //更新被关注的人的粉丝列表
        Query q = new Query(Criteria.where("userId").is(subscriber.getSubscriberId()));
        Fan fanData = mongoTemplate.findOne(q, Fan.class, "fan");
        //添加个人粉丝信息
        UserBasic u = new UserBasic();
        u.setUserId(user.getId());
        u.setImg(user.getImg());
        u.setCreateTime(DateUtil.nowTime());
        u.setUserName(user.getUserName());
        if (null != fanData) {
            fanData.getFanList().add(u);
        } else {
            fanData = new Fan();
            fanData.setId(UUID.randomUUID().toString().replace("-", ""));
            fanData.setUserId(subscriber.getSubscriberId());
            List<UserBasic> fanList = new ArrayList<>();
            fanList.add(u);
            fanData.setFanList(fanList);
        }
        mongoTemplate.save(fanData, "fan");
        log.info("添加粉丝成功,用户名:{},", user.getUserName());

    }
}
