package com.youkeda.comment.service.impl;

import com.youkeda.comment.model.PersonalRecord;
import com.youkeda.comment.service.PersonalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PersonalRecordServiceImpl implements PersonalRecordService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public int save(PersonalRecord personalRecord) {
        Boolean result = redisTemplate.opsForZSet().add("integralRank",personalRecord,personalRecord.getPoints());
        return result ? 1:0;
    }

    @Override
    public void initDefaultData(Long userId) {
        PersonalRecord p = new PersonalRecord();
        p.setPoints(1200);
        p.setEliminateNum(0);
        p.setKd(0d);
        p.setPlayNum(0);
        p.setTopTenTimes(0);
        p.setUserId(userId);
        p.setWinTimes(0);
        save(p);
    }

}
