package com.hiynn.friend.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @author xuxitan
 * @description
 * @date 2020/2/12 11:15
 **/
public class MongoUtil {


    private static MongoTemplate mongoTemplate;

    @Autowired
    public MongoUtil(MongoTemplate mongoTemplate) {
        MongoUtil.mongoTemplate = mongoTemplate;
    }

    /**
     * 保存数据对象，集合为数据对象中@Document 注解所配置的collection
     * insert：若新增数据的主键已经存在，则会抛 org.springframework.dao.DuplicateKeyException 异常提示主键重复，不保存当前数据。
     * save: 若新增数据的主键已经存在，则会对当前已经存在的数据进行修改操作
     *
     * @param obj 数据对象
     */
    public static void insert(Object obj) {
        mongoTemplate.insert(obj);
    }

    /**
     * 保存数据对象到指定对象
     * insert：若新增数据的主键已经存在，则会抛 org.springframework.dao.DuplicateKeyException 异常提示主键重复，不保存当前数据。
     * save: 若新增数据的主键已经存在，则会对当前已经存在的数据进行修改操作
     *
     * @param obj            数据对象
     * @param collectionName 集合名称
     */
    public static void insert(Object obj, String collectionName) {
        mongoTemplate.insert(obj, collectionName);
    }

    /**
     * 保存数据对象，集合为数据对象中@Document 注解所配置的collection
     * insert：若新增数据的主键已经存在，则会抛 org.springframework.dao.DuplicateKeyException 异常提示主键重复，不保存当前数据。
     * save: 若新增数据的主键已经存在，则会对当前已经存在的数据进行修改操作
     *
     * @param obj 数据对象
     */
    public static void save(Object obj) {
        mongoTemplate.save(obj);
    }

    /**
     * 保存数据对象到指定对象
     * insert：若新增数据的主键已经存在，则会抛 org.springframework.dao.DuplicateKeyException 异常提示主键重复，不保存当前数据。
     * save: 若新增数据的主键已经存在，则会对当前已经存在的数据进行修改操作
     *
     * @param obj            数据对象
     * @param collectionName 集合名称
     */
    public static void save(Object obj, String collectionName) {
        mongoTemplate.save(obj, collectionName);
    }

    /**
     * 根据数据对象中的id删除数据，集合为数据对象中@Document 注解所配置的collection
     *
     * @param obj 数据对象
     */
    public static void remove(Object obj) {
        mongoTemplate.remove(obj);
    }

    /**
     * 指定集合 根据数据对象中的id删除数据
     *
     * @param obj            数据对象
     * @param collectionName 集合名
     */
    public static void remove(Object obj, String collectionName) {
        mongoTemplate.remove(obj, collectionName);
    }

    /**
     * 根据key，value到指定集合删除数据
     *
     * @param key            键
     * @param value          值
     * @param collectionName 集合名
     */
    public static void removeByKey(String key, Object value, String collectionName) {
        Query query = Query.query(Criteria.where(key).is(value));
        mongoTemplate.remove(query, collectionName);
    }


    /**
     * 指定集合 根据条件查询出符合的第一条数据
     *
     * @param obj            数据对象
     * @param findKeys       查询条件 key
     * @param findValues     查询条件 value
     * @param collectionName 集合名
     * @return
     */
    public static Object findOne(Object obj, String[] findKeys, Object[] findValues, String collectionName) {

        Criteria criteria = addCondition(findKeys, findValues);
        Query query = Query.query(criteria);
        Object resultObj = mongoTemplate.findOne(query, obj.getClass(), collectionName);
        return resultObj;
    }


    /**
     * 指定集合 根据条件查询出所有结果集
     *
     * @param obj            数据对象
     * @param findKeys       查询条件 key
     * @param findValues     查询条件 value
     * @param collectionName 集合名
     * @return
     */
    public static List<? extends Object> find(Object obj, String[] findKeys, Object[] findValues, String collectionName) {

        Criteria criteria = addCondition(findKeys, findValues);
        Query query = Query.query(criteria);
        List<? extends Object> resultList = mongoTemplate.find(query, obj.getClass(), collectionName);
        return resultList;

    }

    /**
     * 创建条件对象
     *
     * @param findKeys   查询条件 key
     * @param findValues 查询条件 value
     * @return
     */
    private static Criteria addCondition(String[] findKeys, Object[] findValues) {
        Criteria criteria = null;
        for (int i = 0; i < findKeys.length; i++) {
            if (i == 0) {
                criteria = Criteria.where(findKeys[i]).is(findValues[i]);
            } else {
                criteria.and(findKeys[i]).is(findValues[i]);
            }
        }
        return criteria;
    }
}
