package com.youkeda.category;

import com.youkeda.category.control.CategoryApi;
import com.youkeda.category.model.Category;
import com.youkeda.category.model.Result;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.List;

@SpringBootTest
public class YkdTest {
    @Autowired
    private CategoryApi categoryApi;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void contextLoads() {

        Class<?> aClass = null;
        try {
            aClass = Class.forName("com.youkeda.category.control.CategoryApi");
        } catch (ClassNotFoundException e) {
            error("没有创建`com.youkeda.category.control.CategoryApi`类");
            return;
        }

        try {
            aClass = Class.forName("com.youkeda.category.model.Category");
        } catch (ClassNotFoundException e) {
            error("没有创建`com.youkeda.category.model.Category`类");
            return;
        }

        Category category = new Category();
        category.setId("00001");
        category.setDescription("测试");
        category.setName("分类1");
        category.setParentCategoryId("000012");
        Result<Category> result = categoryApi.add(category);
        if (result == null || !result.getSuccess() || result.getData() == null) {
            error("插入类目失败");
        }
    }

    public static void error(String msg) {
        System.err.println("<YkdError>" + msg + "</YkdError>");
    }
}
