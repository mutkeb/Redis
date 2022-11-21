package com.youkeda.category;

import com.youkeda.category.control.CategoryApi;
import com.youkeda.category.model.Category;
import com.youkeda.category.model.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

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

        Result<Category> queryAllResult = categoryApi.get("gcl_001");

        if (queryAllResult == null) {
            error("get(\"gcl_001\") 返回值不能为 null");
            return;
        }

        Category gcl001 = queryAllResult.getData();

        if (gcl001 == null) {
            error("请插入 gcl_001 女装 类目数据");
            return;
        }

        if (CollectionUtils.isEmpty(gcl001.getSubCategoryList())) {
            error("请插入 gcl_001 女装 的子类目数据");
            return;
        }
    }

    public static void error(String msg) {
        System.err.println("<YkdError>" + msg + "</YkdError>");
    }
}
