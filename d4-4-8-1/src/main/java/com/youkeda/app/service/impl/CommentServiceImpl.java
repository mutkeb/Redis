package com.youkeda.app.service.impl;

import com.youkeda.app.model.Comment;
import com.youkeda.app.service.CommentService;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * @date 2020/6/16, 周二
 */
@Service
public class CommentServiceImpl implements CommentService {
    
    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public Long add(Comment comment) {
        comment.setId(getAutoIncrId());
        if (getAutoIncrId() !=null && getAutoIncrId().length() != 16){
            return 0L;
        }

        Long listLength = redisTemplate.opsForList().leftPush("comments", comment);
        return listLength;
    }

    // 获取自增长 id
    private String getAutoIncrId() {
        //  格式化格式为年月日
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        //  获取当前时间
        String now = LocalDateTime.now().format(dateTimeFormatter);
        //  通过redis的自增获取序号
        RAtomicLong atomicLong = redissonClient.getAtomicLong(now);
        atomicLong.expire(1,TimeUnit.DAYS);
        long number = atomicLong.incrementAndGet();
        //  拼装订单号
        String commentID = now + String.format("%08d",number);
        return commentID;
    }

}
