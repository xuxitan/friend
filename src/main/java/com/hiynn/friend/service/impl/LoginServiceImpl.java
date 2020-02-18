package com.hiynn.friend.service.impl;

import com.hiynn.friend.dto.LoginDTO;
import com.hiynn.friend.entity.User;
import com.hiynn.friend.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

/**
 * @author xuxitan
 * @description
 * @date 2020/2/17 16:30
 **/
@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /***
     * 描述 登录
     * @author xuxitan
     * @date 2020/2/17 16:30
     * @param login
     * @return void
     */
    @Override
    public void login(LoginDTO login) {
        Query query = new Query();
        query.addCriteria(new Criteria().andOperator(Criteria.where("userName").is(login.getUserName()), Criteria.where("password").is(login.getPassword())));
        User one = mongoTemplate.findOne(query, User.class);
        if(null == one){
            throw new RuntimeException("用户名或密码有误");
        }
        //登录成功设置为激活
        one.setActive(1);
        //更新
        mongoTemplate.save(one);
    }
}
