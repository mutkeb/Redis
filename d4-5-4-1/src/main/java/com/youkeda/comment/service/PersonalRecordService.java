package com.youkeda.comment.service;

import com.youkeda.comment.model.PersonalRecord;

public interface PersonalRecordService {
    /**
     * 添加或者修改个人战绩
     *
     * @param personalRecord 个人战绩
     * @return PersonalRecord
     */
    int save(PersonalRecord personalRecord);

    /**
     * 初始化默认的个人战绩
     *
     * @param userId 用户主键 id
     */
    void initDefaultData(Long userId);
}
