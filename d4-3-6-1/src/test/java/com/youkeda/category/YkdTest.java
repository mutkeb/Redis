package com.youkeda.category;

import com.youkeda.category.control.CategoryApi;
import com.youkeda.category.model.Category;
import com.youkeda.category.model.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import java.util.List;

@SpringBootTest
public class YkdTest {
    @Autowired
    private CategoryApi categoryApi;

    @Test
    void contextLoads() {
        Class<?> aClass = null;
        try {
            aClass = Class.forName("com.youkeda.category.control.CategoryApi");
        } catch (ClassNotFoundException e) {
            error("没有创建`com.youkeda.app.control.CategoryApi`类");
            return;
        }

        Result<List<Category>> queryAllResult = categoryApi.queryAll();

        if (queryAllResult == null || CollectionUtils.isEmpty(queryAllResult.getData())) {
            error("queryAll()查询结果出错了，没有查询到任何数据，请检查数据和程序。");
            return;
        }

        List<Category> categories = queryAllResult.getData();

        for (Category cat : categories) {
            if (cat.getGmtCreated() == null) {
                error(cat.getName() + " 的 gmtCreated 不能为 null，请设置为当前时间");
            }
            if (cat.getGmtModified() == null) {
                error(cat.getName() + " 的 gmtModified 不能为 null，请设置为当前时间");
            }
        }
    }

    public static void error(String msg) {
        System.err.println("<YkdError>" + msg + "</YkdError>");
    }
}
