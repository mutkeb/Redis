package com.youkeda.category.control;

import com.youkeda.category.model.Category;
import com.youkeda.category.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class CategoryApi {
    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/create")
    @ResponseBody
    public Result<Category> add(@RequestBody Category category) {
        Result result = new Result();
        redisTemplate.opsForList().leftPush("categoryList",category);
        result.setSuccess(true);
        
        result.setData(category);
        return result;
    }
}
