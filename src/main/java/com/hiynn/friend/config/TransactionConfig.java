package com.hiynn.friend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.stereotype.Component;

/**
 * @author xuxitan
 * @description
 * @date 2020/2/14 16:17
 **/
//@Component
public class TransactionConfig {

    @Bean
    MongoTransactionManager transactionManager(MongoDbFactory factory) {
        return new MongoTransactionManager(factory);
    }

}
