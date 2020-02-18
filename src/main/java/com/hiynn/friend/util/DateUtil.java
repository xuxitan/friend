package com.hiynn.friend.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author xuxitan
 * @description
 * @date 2020/2/17 17:11
 **/
public class DateUtil {

    /**
     * 返回当前的时间 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String nowTime() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }
}
