package com.hiynn.friend;

import com.alibaba.fastjson.JSON;
import com.hiynn.friend.entity.Moment;
import com.hiynn.friend.entity.Replay;
import com.hiynn.friend.entity.TimeLine;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.*;

@SpringBootTest
class MongoApplicationTests {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void addColumn(){
        Query query = new Query(Criteria.where("id").is("TM1001"));
        TimeLine one = mongoTemplate.findOne(query, TimeLine.class);
        one.setUserId("131314");
        mongoTemplate.save(one,"timeLine");
        System.out.println(one);
    }

    @Test
    public void batch() {
        //数据集合
        List<TimeLine> list = new ArrayList<>();

        TimeLine timeLine = new TimeLine();
        timeLine.setUserId("123");
        timeLine.setId(UUID.randomUUID().toString().replace("-", ""));
        timeLine.setMomentId("1332");
        //好友
        timeLine.setIsOwn(0);
        timeLine.setIsShow(1);
        timeLine.setCreateTime(new Date());
        list.add(timeLine);
        timeLine = new TimeLine();
        timeLine.setUserId("123");
        timeLine.setId(UUID.randomUUID().toString().replace("-", ""));
        timeLine.setMomentId("1332");
        //好友
        timeLine.setIsOwn(0);
        timeLine.setIsShow(1);
        timeLine.setCreateTime(new Date());
        list.add(timeLine);
        //添加集合
        mongoTemplate.insert(list, "timeLine");
}


    @Test
    public void add() {
//        Subscriber subscriber = new Subscriber();
//        subscriber.setId("3643uy464dg");
//        subscriber.setUserId("U123");
//        subscriber.setSubscriberList(Arrays.asList("U456", "U789"));
//        mongoTemplate.save(subscriber);

        //指定删除元素
        Query query = new Query(Criteria.where("userId").is("U123"));
        Update update = new Update().pull("subscriberList", "23");
        mongoTemplate.updateFirst(query, update, "subscriber");
    }

    //添加元素
    @Test
    public void push() {
        Query query = new Query(Criteria.where("_id").is("M1001"));
        Replay r1 = new Replay();
        r1.setFrom("U456");
        r1.setTo("U123");
        r1.setContent("天气好，你准备干啥去");
        Update update = new Update().push("replay", r1);

        mongoTemplate.updateFirst(query, update, "moment");
    }

    @Test
    void contextLoads() {

        TimeLine timeline = new TimeLine();
        timeline.setId("TM1001");
        timeline.setUserId("U123");
        timeline.setMomentId("M1001");
        timeline.setIsShow(1);
        timeline.setIsOwn(1);
        timeline.setCreateTime(new Date());


        Moment moment = new Moment();
        moment.setId("M1001");
        moment.setImage("123.jpg");
        moment.setTopic("天气状况");
        moment.setContent("今天的天气挺好");
        moment.setPraises(50);
        List<String> list = new ArrayList<>();
        list.add("U123");
        list.add("U456");
        List<Replay> replayList = new ArrayList<>();
        Replay r1 = new Replay();
        r1.setFrom("U123");
        r1.setTo("U456");
        r1.setContent("我准备去钓鱼,你呢");
        replayList.add(r1);
        moment.setReplay(replayList);
        moment.setPraiseList(list);
        moment.setCreateTime(new Date());
        mongoTemplate.save(moment, "moment");
//        mongoTemplate.save(timeline);
    }


    @Test
    public void dd() {
        LookupOperation query2 = LookupOperation.newLookup().
                from("moment"). //关联表
                localField("momentId"). //当前表的关联id
                foreignField("_id")  //主表的id
                .as("momentDetail"); //别名

        //主表的过滤条件
        Criteria criteria = Criteria.where("momentId").is("M1001");
        AggregationOperation match = Aggregation.match(criteria);


        Aggregation aggregation = Aggregation.newAggregation(match, query2);

        List<TimeLine> timeLine = mongoTemplate.aggregate(aggregation, "timeLine", TimeLine.class).getMappedResults();
        System.out.println(JSON.toJSONString(timeLine));
    }
}
