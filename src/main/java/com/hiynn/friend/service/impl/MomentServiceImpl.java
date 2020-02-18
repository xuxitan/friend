package com.hiynn.friend.service.impl;

import cn.jpush.api.report.UsersResult;
import com.hiynn.friend.dto.CommentDTO;
import com.hiynn.friend.dto.JPushReqParam;
import com.hiynn.friend.dto.MomentDTO;
import com.hiynn.friend.entity.*;
import com.hiynn.friend.service.JpushService;
import com.hiynn.friend.service.MomentService;
import com.hiynn.friend.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author xuxitan
 * @description
 * @date 2020/2/14 14:45
 **/
@Slf4j
@Service
public class MomentServiceImpl implements MomentService {


    @Autowired
    private MongoTemplate mongoTemplate;

    @Resource
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    private JpushService jpushService;

    @Value("${image.path}")
    private String imgPath;

    /***
     * 描述 给朋友圈评论回复
     * @author xuxitan
     * @date 2020/2/17 10:48
     * @param comment
     * @return void
     */
    @Override
    public void commentMoment(CommentDTO comment) {
        //先查询该朋友圈信息
        Query query = new Query(Criteria.where("id").is(comment.getMomentId()));
        Moment moment = mongoTemplate.findOne(query, Moment.class, "moment");
        if (null != moment) {
            //评论的用户信息
            Query q = new Query(Criteria.where("id").is(comment.getFromId()));
            User user = mongoTemplate.findOne(q, User.class);
            if (null == user) {
                throw new RuntimeException("用户不存在");
            }
            //评论数加1
            moment.setMoments(moment.getMoments() + 1);
            //添加评论信息
            Replay replay = new Replay();
            replay.setContent(comment.getContent());
            replay.setFrom(comment.getFromId());
            replay.setFromImg(user.getImg());
            replay.setFromUserName(user.getUserName());
            replay.setTo(comment.getToId());
            replay.setTime(DateUtil.nowTime());
            if (null == moment.getReplay()) {
                List<Replay> list = new ArrayList<>();
                list.add(replay);
                moment.setReplay(list);
            } else {
                moment.getReplay().add(0,replay);
            }
            //有则改之,无则加之
            mongoTemplate.save(moment, "moment");

            JPushReqParam reqParam = new JPushReqParam();
            reqParam.setTitle("评论通知");
            reqParam.setContent("测试用户评论了您");
            //如果给该朋友圈作者评论，则只通知作者
            if (moment.getUserId().equals(comment.getToId())) {
                reqParam.setPersonIds(new String[]{comment.getToId()});
            } else {
                //如果给评论的人二次评论，则通知作者和该评论的人
                reqParam.setPersonIds(new String[]{comment.getFromId(), comment.getToId()});
            }
            //评论通知
            jpushService.sendPersonMessage(reqParam);
        }
    }

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
            //点赞的用户信息
            Query q = new Query(Criteria.where("id").is(userId));
            User user = mongoTemplate.findOne(q, User.class);
            if (null == user) {
                throw new RuntimeException("用户不存在");
            }
            //点赞数加1
            moment.setPraises(moment.getPraises() + 1);
            UserBasic userBasic = new UserBasic();
            userBasic.setId(userId);
            userBasic.setImg(user.getImg());
            userBasic.setTime(DateUtil.nowTime());
            userBasic.setUserName(user.getUserName());
            if (null == moment.getPraiseList()) {
                List<UserBasic> praiseList = new ArrayList<>();
                praiseList.add(0, userBasic);
                moment.setPraiseList(praiseList);
            } else {
                moment.getPraiseList().add(0, userBasic);
            }

            //有则改之,无则加之
            mongoTemplate.save(moment, "moment");

            //给发朋友圈的人发点赞通知
            JPushReqParam reqParam = new JPushReqParam();
            reqParam.setTitle("点赞通知");
            reqParam.setContent("测试用户赞了您");
            reqParam.setPersonIds(new String[]{moment.getUserId()});
            //点赞通知
            jpushService.sendPersonMessage(reqParam);
        }
    }

    /**
     * 文件传输
     *
     * @param file
     * @param path
     * @param fileName
     */
    private static void transfer(MultipartFile file, String path, String fileName) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path + fileName);
            out.write(file.getBytes());
            out.flush();
            log.info("上传文件成功:{}", fileName);
        } catch (Exception e) {
            log.error("上传文件失败:" + e.getMessage());
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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
        String fileName = null;
        if (momentDTO.getFile() != null) {
            //上传文件
            String originalFilename = momentDTO.getFile().getOriginalFilename();
            assert originalFilename != null;
            String[] fileStr = originalFilename.split("\\.");
            String fileFormat = fileStr[fileStr.length - 1];
            //新的文件名
            fileName = System.currentTimeMillis() + "." + fileFormat;
            //传输
            transfer(momentDTO.getFile(), imgPath, fileName);
        }
        //保存朋友圈信息
        Moment moment = new Moment();
        moment.setId(UUID.randomUUID().toString().replace("-", ""));
        moment.setUserId(momentDTO.getUserId());
        moment.setContent(momentDTO.getContent());
        moment.setTopic(momentDTO.getTopic());
        moment.setIsShow(momentDTO.getIsShow());
        moment.setImage(fileName);
        moment.setPraises(0);
        moment.setMoments(0);
        moment.setIsOwn(1);
        moment.setCreateTime(DateUtil.nowTime());
        //保存进本的朋友圈信息
        mongoTemplate.save(moment, "moment");

        //先把自己添加到时间线中
        TimeLine timeLine = new TimeLine();
        timeLine.setUserId(moment.getUserId());
        timeLine.setId(UUID.randomUUID().toString().replace("-", ""));
        timeLine.setMomentId(moment.getId());
        timeLine.setIsOwn(moment.getIsOwn());
        timeLine.setIsShow(moment.getIsShow());
        timeLine.setCreateTime(DateUtil.nowTime());
        mongoTemplate.save(timeLine, "timeLine");

        //更新自己发的朋友圈信息数量
        Query query = new Query(Criteria.where("id").is(momentDTO.getUserId()));
        User user = mongoTemplate.findOne(query, User.class);
        if (null != user) {
            user.setMoments(user.getMoments() + 1);
            mongoTemplate.save(user);
            log.info("更新自己发布的朋友圈数量为:{}", user.getMoments());
        }

        //异步将该朋友圈推送到作者的所有好友时间线上
        jmsMessagingTemplate.convertAndSend("queue_time_line", moment);
    }
}
