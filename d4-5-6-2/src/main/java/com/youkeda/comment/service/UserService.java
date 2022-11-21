package com.youkeda.comment.service;

import com.youkeda.comment.model.Result;
import com.youkeda.comment.model.User;

import java.util.List;

public interface UserService {

    String CACHE_KEY = "integralRankUser";

    String CACHE_MODEL_KEY = "integralRankUserModel";

    /**
     * 注册用户
     *
     * @param userName
     * @param pwd
     * @return
     */
    public Result<User> register(String userName, String pwd);

    /**
     * 执行登录逻辑，登录成功返回 User 对象
     *
     * @param userName
     * @param pwd
     * @return
     */
    public Result<User> login(String userName, String pwd);

    /**
     * 通过主键查询用户
     * @param id
     * @return
     */
    public User findById(Long id);


}
