package com.youkeda.category.control;

import com.youkeda.category.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/test")
public class CategoryTestApi {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CategoryApi categoryApi;

    @GetMapping(path = "/create")
    public boolean testAddCategory() {
        Category category = new Category();
        category.setId("gcl_001");
        category.setName("女装");
        category.setDescription("女装");
        categoryApi.add(category);

        category = new Category();
        category.setId("gcl_sub_001");
        category.setName("连衣裙");
        category.setParentCategoryId("gcl_001");
        category.setDescription("连衣裙");
        categoryApi.add(category);

        category = new Category();
        category.setId("gcl_sub_002");
        category.setName("半身裙");
        category.setParentCategoryId("gcl_001");
        category.setDescription("半身裙");
        categoryApi.add(category);

        category = new Category();
        category.setId("gcl_sub_003");
        category.setName("毛针织衫");
        category.setParentCategoryId("gcl_001");
        category.setDescription("毛针织衫");
        categoryApi.add(category);

        category = new Category();
        category.setId("gcl_sub_004");
        category.setName("卫衣");
        category.setParentCategoryId("gcl_001");
        category.setDescription("卫衣");
        categoryApi.add(category);

        category = new Category();
        category.setId("bg_001");
        category.setName("箱包");
        category.setDescription("箱包");
        categoryApi.add(category);

        category = new Category();
        category.setId("bg_sub_001");
        category.setName("女包");
        category.setParentCategoryId("bg_001");
        category.setDescription("女包");
        categoryApi.add(category);

        category = new Category();
        category.setId("bg_sub_002");
        category.setName("双肩包");
        category.setParentCategoryId("bg_001");
        category.setDescription("双肩包");
        categoryApi.add(category);

        category = new Category();
        category.setId("bg_sub_003");
        category.setName("链条包");
        category.setParentCategoryId("bg_001");
        category.setDescription("链条包");
        categoryApi.add(category);

        category = new Category();
        category.setId("bg_sub_004");
        category.setName("斜挎包");
        category.setParentCategoryId("bg_001");
        category.setDescription("斜挎包");
        categoryApi.add(category);

        category = new Category();
        category.setId("mcl_001");
        category.setName("男装");
        category.setDescription("男装");
        categoryApi.add(category);

        category = new Category();
        category.setId("mcl_sub_001");
        category.setName("衬衫");
        category.setParentCategoryId("mcl_001");
        category.setDescription("衬衫");
        categoryApi.add(category);

        category = new Category();
        category.setId("mcl_sub_002");
        category.setName("休闲裤");
        category.setParentCategoryId("mcl_001");
        category.setDescription("休闲裤");
        categoryApi.add(category);

        category = new Category();
        category.setId("mcl_sub_003");
        category.setName("夹克");
        category.setParentCategoryId("mcl_001");
        category.setDescription("夹克");
        categoryApi.add(category);

        category = new Category();
        category.setId("mcl_sub_004");
        category.setName("西装");
        category.setParentCategoryId("mcl_001");
        category.setDescription("西装");
        categoryApi.add(category);

        return true;
    }

    @GetMapping(path = "/get")
    public Category getCategory(@RequestParam("index") int index) {
        Long size = redisTemplate.opsForList().size("categoryList");
        System.out.println(size);

        Category category = (Category)redisTemplate.opsForList().index("categoryList", index);

        List<Category> categoryDatas = redisTemplate.opsForList().range("categoryList", 0, 0);

        return category;
    }

    @GetMapping(path = "/range")
    public List<Category> getCategorys(@RequestParam("start") long start, @RequestParam("end") long end) {

        List<Category> categoryDatas = redisTemplate.opsForList().range("categoryList", start, end);

        return categoryDatas;
    }


    @GetMapping(path = "/replenish")
    public List<Category> replenishCategorys() {
        //  得到所有类目
        List<Category> categoryList = getCategorys(0,-1);
        //  补充时间属性
        for(int i = 0; i < categoryList.size(); i++) {
            categoryList.get(i).setGmtCreated(LocalDateTime.now());
            categoryList.get(i).setGmtModified(LocalDateTime.now());
            redisTemplate.opsForList().set("categoryList",i,categoryList.get(i));
        }
        return categoryList;
    }

}
