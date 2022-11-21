package com.youkeda.category;

import com.youkeda.category.control.CategoryApi;
import com.youkeda.category.model.Category;
import com.youkeda.category.model.Result;
import org.apache.commons.lang3.StringUtils;
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
        Result<List<Category>> queryAllResult = categoryApi.queryAll();

        if (queryAllResult == null) {
            error("queryAll() 返回值不能为 null");
            return;
        }

        List<Category> categorys = queryAllResult.getData();

        if (CollectionUtils.isEmpty(queryAllResult.getData())) {
            error("未查询到任何类目信息");
            return;
        }

        Category gcl001 = null;
        for (Category category : categorys) {
            if (StringUtils.equals(category.getId(), "gcl_001")) {
                gcl001 = category;
                break;
            }
        }

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
