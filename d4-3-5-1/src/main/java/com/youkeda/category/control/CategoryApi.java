package com.youkeda.category.control;

import com.youkeda.category.model.Category;
import com.youkeda.category.model.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CategoryApi {
    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/create")
    @ResponseBody
    public Result<Category> add(@RequestBody Category category) {
        Result result = new Result();
        result.setSuccess(true);
        redisTemplate.opsForList().leftPush("categoryList", category);
        result.setData(category);
        return result;
    }

    @GetMapping("/queryAll")
    @ResponseBody
    public Result<List<Category>> queryAll(){
        //  先找出所有的category
        List<Category> categoryList = redisTemplate.opsForList().range("categoryList", 0, -1);
        //  开始遍历，用Map类型存储id对应的category
        Map<String,Category> catsMap = new HashMap<>();
        categoryList.forEach(category -> {
            catsMap.put(category.getId(),category);
        });
        //初始化一个虚拟根节点，其subCategoryList就是所有的一级类目
        catsMap.put("0", new Category());
        //  再次遍历，设置树结构
        categoryList.forEach(category -> {
            //  父类Id
            String parentCategoryId = category.getParentCategoryId();
            if(StringUtils.isBlank(parentCategoryId)){
                parentCategoryId = "0";
            }
            //  得到父类目
            Category category1 = catsMap.get(parentCategoryId);
            //  假如其父类的列表为空，创建新列表
            if(category1 != null){

                if(category1.getSubCategoryList() == null){
                    category1.setSubCategoryList(new ArrayList<>());
                }
                category1.getSubCategoryList().add(category);
            }
        });
        Result<List<Category>> result = new Result<>();
        result.setSuccess(true);
        result.setData(catsMap.get("0").getSubCategoryList());
        return result;
    }
}
