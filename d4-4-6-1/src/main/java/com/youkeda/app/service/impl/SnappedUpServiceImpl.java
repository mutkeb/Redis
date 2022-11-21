package com.youkeda.app.service.impl;

import com.youkeda.app.dao.ProductDAO;
import com.youkeda.app.dataobject.ProductDO;
import com.youkeda.app.model.Result;
import com.youkeda.app.service.SnappedUpService;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.redisson.api.RedissonClient;
@Service
public class SnappedUpServiceImpl implements SnappedUpService {
    private static final Logger LOG = LoggerFactory.getLogger(SnappedUpServiceImpl.class);

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Result<Boolean> snappedUp(Long id) {
        //初始化返回数据
        Result result = new Result();

        if (id == null || id < 1) {
            result.setMessage("缺少参数，必须输入商品id");
            result.setSuccess(false);
            return result;
        }
        //  取得锁
        RLock rLock = redissonClient.getLock(id + "-1-stock");
        //  上锁
        rLock.lock();

        Integer stock = (Integer)redisTemplate.opsForValue().get(getKey(id));

        // 没有缓存库存，则从数据库查询
        if (stock == null) {
            //去数据库查询该商品的信息
            ProductDO product = productDAO.selectById(id);
            // 商品是否存在
            if (product == null) {
                result.setMessage("商品不存在。id=" + id);
                result.setSuccess(false);
                return result;
            }

            //将信息缓存到redis里边
            stock = product.getStock();
            redisTemplate.opsForValue().set(getKey(id), stock);
        }

        // 判断该商品的库存是否大于1
        if (stock < 1 || stock == null) {
            result.setMessage("商品已购完。");
            result.setSuccess(false);
            return result;
        }
        try {
            // 缓存的库存减 1
            redisTemplate.opsForValue().set(getKey(id), (stock - 1));
            // 数据库的库存减 1
            productDAO.reduceStock(id, 1);
        }catch (Exception e){
            LOG.error("fail");
        }finally {
            rLock.unlock();
        }
        // 插入一条订单数据。
        // 由于在我们这里不是重要学习逻辑，可以不写，仅作为逻辑标注

        result.data(true);
        result.setSuccess(true);
        return result;
    }

    // 纯数字做key，不容易理解作用和含义，也容易跟其它模型冲突。加个前缀就明确了。
    private String getKey(Long id) {
        return "productId-" + String.valueOf(id) + "-stock";
    }

}
