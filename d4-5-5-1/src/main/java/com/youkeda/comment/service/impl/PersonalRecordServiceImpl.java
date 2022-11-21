package com.youkeda.comment.service.impl;

import com.youkeda.comment.model.PersonalRecord;
import com.youkeda.comment.service.PersonalRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LoggerGroup;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PersonalRecordServiceImpl implements PersonalRecordService {
    private static final Logger LOG = LoggerFactory.getLogger(PersonalRecordServiceImpl.class);

    private static final String KEY = "integralRank";

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public int save(PersonalRecord personalRecord) {
        Boolean result = redisTemplate.opsForZSet().add(KEY, personalRecord, personalRecord.getPoints());
        return result ? 1 : 0;
    }

    @Override
    public void initDefaultData(Long userId) {
        //创建用户时 默认会创建个人战绩
        PersonalRecord personalRecord = new PersonalRecord();
        personalRecord.setWinTimes(0);
        personalRecord.setTopTenTimes(0);
        personalRecord.setEliminateNum(0);
        personalRecord.setPlayNum(0);
        personalRecord.setPoints(1200);
        personalRecord.setKd(0d);
        personalRecord.setUserId(userId);

        save(personalRecord);
    }

    @Override
    public List<PersonalRecord> queryLimit(Long limitNum) {
        Set<TypedTuple<PersonalRecord>> sets = null;
        if(limitNum == 0 || limitNum == null){
            limitNum = -1L;
            sets = redisTemplate.opsForZSet().reverseRangeWithScores(KEY, 0, -1);
        }else{
            sets = redisTemplate.opsForZSet().reverseRangeWithScores(KEY, 0, limitNum-1);
        }
        List<PersonalRecord> list = new ArrayList<>();
        sets.forEach(set->{
            PersonalRecord p = set.getValue();
            list.add(p);
        });
        return list;
    }

}
