package com.hiynn.friend.service.impl;

import com.hiynn.friend.dto.MomentDTO;
import com.hiynn.friend.entity.Moment;
import com.hiynn.friend.entity.TimeLine;
import com.hiynn.friend.service.MomentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author xuxitan
 * @description
 * @date 2020/2/14 14:45
 **/
@Service
public class MomentServiceImpl implements MomentService {


    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private JmsTemplate jmsTemplate;

    /***
     * 描述 给朋友圈点赞
     * @author xuxitan
     * @date 2020/2/14 16:53
     * @param momentId
     * @param userId 点赞的用户
     * @return void
     */
    @Override
    public void praiseMoment(String momentId, String userId) {
        Query query = new Query(Criteria.where("id").is(momentId));
        Moment moment = mongoTemplate.findOne(query, Moment.class, "moment");
        if (null != moment) {
            moment.setPraises(moment.getPraises() + 1);
            if (null == moment.getPraiseList()) {
                List<String> praiseList = new ArrayList<>();
                praiseList.add(0, userId);
            } else {
                moment.getPraiseList().add(0, userId);
            }
        }
        //有则改之,无则加之
        mongoTemplate.save(moment, "moment");

    }


    /***
     * 描述 发朋友圈并添加到时间线里
     * @author xuxitan
     * @date 2020/2/14 14:49
     * @param momentDTO
     * @return void
     */
    @Override
    @Transactional
    public void saveMomentAndTimeLine(MomentDTO momentDTO) {
        if (momentDTO.getFile() != null) {
            //上传文件
        }
        //保存朋友圈信息
        Moment moment = new Moment();
        moment.setId(UUID.randomUUID().toString().replace("-", ""));
        moment.setUserId(momentDTO.getUserId());
        moment.setContent(momentDTO.getContent());
        moment.setTopic(momentDTO.getTopic());
        moment.setIsShow(momentDTO.getIsShow());
        moment.setPraises(0);
        moment.setMoments(0);
        moment.setIsOwn(1);
        moment.setImage("A.jpg");
        moment.setCreateTime(new Date());
        //保存进本的朋友圈信息
        mongoTemplate.save(moment, "moment");

        //先把自己添加到时间线中
        TimeLine timeLine = new TimeLine();
        timeLine.setUserId(moment.getUserId());
        timeLine.setId(UUID.randomUUID().toString().replace("-", ""));
        timeLine.setMomentId(moment.getId());
        timeLine.setIsOwn(moment.getIsOwn());
        timeLine.setIsShow(moment.getIsShow());
        timeLine.setCreateTime(new Date());
        mongoTemplate.save(timeLine, "timeLine");

        //异步将该朋友圈推送到作者的所有好友时间线上
        jmsTemplate.convertAndSend("queue_time_line", moment);
    }
}
