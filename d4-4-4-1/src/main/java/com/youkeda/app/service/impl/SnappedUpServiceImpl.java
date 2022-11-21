package com.youkeda.app.service.impl;

import com.youkeda.app.dao.ProductDAO;
import com.youkeda.app.dataobject.ProductDO;
import com.youkeda.app.model.Result;
import com.youkeda.app.service.SnappedUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class SnappedUpServiceImpl implements SnappedUpService {
    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Result<Boolean> snappedUp(Long id) {
        //初始化返回数据
        Result result = new Result();
        //  获得key
        String key = getKey(id);
        //  从Redis中查询对应的库存
        ProductDO productDO = (ProductDO) redisTemplate.opsForValue().get(key);
        //  从Redis查询为空
        if(productDO == null){
            productDO = productDAO.selectById(id);
            redisTemplate.opsForValue().set(key,productDO.getStock());
        }
        //  若商品已经售罄
        if(productDO.getStock() == 0){
            result.setSuccess(false);
            result.setData(false);
            result.setMessage("已售完");
            return result;
        }
        redisTemplate.opsForValue().set(key,productDO.getStock()-1);
        productDAO.reduceStock(id,1);
        result.data(true);
        result.setSuccess(true);
        return result;
    }

    // 纯数字做key，不容易理解作用和含义，也容易跟其它模型冲突。加个前缀就明确了。
    private String getKey(Long id) {
        return "productId-" + String.valueOf(id) + "-stock";
    }

}
