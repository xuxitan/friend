package com.hiynn.friend.listener;

import com.alibaba.fastjson.JSON;
import com.hiynn.friend.entity.Moment;
import com.hiynn.friend.entity.Subscriber;
import com.hiynn.friend.entity.TimeLine;
import com.hiynn.friend.entity.UserBasic;
import com.hiynn.friend.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 时间线监听
 *
 * @author xuxitan
 * @description
 * @date 2020/2/14 15:08
 **/
@EnableJms
@Component
@Slf4j
public class TimeLineListener {

    @Autowired
    private MongoTemplate mongoTemplate;

    @JmsListener(destination = "queue_time_line")
    public void handleTimeLine(String message) {
        log.info("时间线监听信息:【{}】", message);
        Moment moment = JSON.parseObject(message, Moment.class);
        Query query = new Query(Criteria.where("userId").is(moment.getUserId()));
        //先查询好友列表
        Subscriber subscriber = mongoTemplate.findOne(query, Subscriber.class, "subscriber");

        //遍历给添加到所有好友的时间线中
        if (null != subscriber && null != subscriber.getSubscriberList()) {
            //数据集合
            List<TimeLine> list = new ArrayList<>();
            for (UserBasic userBasic : subscriber.getSubscriberList()) {
                TimeLine timeLine = new TimeLine();
                //关注的好友的
                timeLine.setUserId(userBasic.getUserId());
                //实际发朋友圈的用户
                timeLine.setRealityId(subscriber.getUserId());
                timeLine.setId(UUID.randomUUID().toString().replace("-", ""));
                timeLine.setMomentId(moment.getId());
                //好友
                timeLine.setIsOwn(0);
                timeLine.setIsShow(moment.getIsShow());
                timeLine.setCreateTime(DateUtil.nowTime());
                list.add(timeLine);
            }
            //添加集合
            mongoTemplate.insert(list, "timeLine");

            log.info("好友时间线同步完成");
        }
    }
}
