package com.youkeda.category.control;

import com.youkeda.category.model.Category;
import com.youkeda.category.model.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CategoryApi {
    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping(path = "/create")
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
    public Result<List<Category>> queryAll() {
        Result<List<Category>> result = new Result<>();
        result.setSuccess(true);

        // 查询所有的类目数据
        List<Category> categoryDatas = redisTemplate.opsForList().range("categoryList", 0, -1);

        //处理父子类目关系
        List<Category> allFirstCats = handleParentData(categoryDatas);
        result.setData(allFirstCats);
        return result;
    }

    /**
     * 处理父子类目关系数据
     *
     * @param categoryDatas 一组类目数据
     */
    private List<Category> handleParentData(List<Category> categoryDatas) {
        if (CollectionUtils.isEmpty(categoryDatas)) {
            // 防止空指针异常，返回一个没有元素的 List
            return new ArrayList<>();
        }

        // 存储类目的 Map
        Map<String, Category> catsMap = new HashMap<>();
        //初始化一个虚拟根节点，其subCategoryList就是所有的一级类目
        catsMap.put("0", new Category());

        categoryDatas.forEach(category -> {
            // 全部放入Map
            catsMap.put(category.getId(), category);
        });

        // 再次遍历模型，处理父子关系
        categoryDatas.forEach(category -> {
            // 得到父实例
            String pId = category.getParentCategoryId();

            // 没有父类目 Id，当做 0 处理，作为虚拟根节点的子类目，就是所有的一级类目
            if (StringUtils.isBlank(pId)) {
                pId = "0";
            }

            Category parentCat = catsMap.get(pId);
            if (parentCat != null) {
                // 未初始化则初始化
                if (parentCat.getSubCategoryList() == null) {
                    parentCat.setSubCategoryList(new ArrayList<>());
                }

                // 当前的类目对象放入类类目的 subCategoryList
                parentCat.getSubCategoryList().add(category);
            }
        });

        List<Category> allFirstCats = catsMap.get("0").getSubCategoryList();

        return allFirstCats;
    }
    //  根据id查询类目
    @GetMapping("/get")
    @ResponseBody
    public Result<Category> get(@RequestParam("id") String id){
        //  先得到所有的category
        List<Category> categoryList = redisTemplate.opsForList().range("categoryList", 0, -1);
        //  利用Mpa数据存储catrgory对象和id的对应关系
        Map<String,Category> catsMap = new HashMap<>();
        //  遍历添加进Mao中
        categoryList.forEach(category -> {
            catsMap.put(category.getId(),category);
        });
        //  首先取出我要找的category
        Category category = catsMap.get(id);
        Result<Category> result = new Result<>();
        if(category == null){
            result.setSuccess(false);
            result.setData(category);
            return result;
        }
        //  先查看其是否有子列目
        if(category.getSubCategoryList() == null){
            category.setSubCategoryList(new ArrayList<>());
        }
        //  遍历所有类目，找到其父类目为所找类目
        categoryList.forEach(category1 -> {
            if(!StringUtils.isEmpty(category1.getParentCategoryId()) &&category1.getParentCategoryId().equals(category.getId()) ){
                category.getSubCategoryList().add(category1);
            }
        });
        result.setData(category);
        result.setSuccess(true);
        return result;
    }
}
