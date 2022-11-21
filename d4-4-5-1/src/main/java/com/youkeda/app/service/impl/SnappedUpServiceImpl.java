package com.youkeda.app.service.impl;

import com.youkeda.app.dao.ProductDAO;
import com.youkeda.app.dataobject.ProductDO;
import com.youkeda.app.model.Result;
import com.youkeda.app.service.SnappedUpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;

@Service
public class SnappedUpServiceImpl implements SnappedUpService {
    private static final Logger LOG = LoggerFactory.getLogger(SnappedUpServiceImpl.class);

    @Autowired
    private ProductDAO productDAO;

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
        try {
            excuteTransaction(result,id);
        }catch (Exception e){
            LOG.error("redisTemplate.excute() error.",e);
        }
        return result;
    }

    // 纯数字做key，不容易理解作用和含义，也容易跟其它模型冲突。加个前缀就明确了。
    private String getKey(Long id) {
        return "productId-" + String.valueOf(id) + "-stock";
    }

    private void excuteTransaction(Result result,Long id){
        String key = getKey(id);
        redisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public List<Object> execute(RedisOperations redisOperations) throws DataAccessException {
                Integer stock = (Integer)redisOperations.opsForValue().get(key);
                if (stock < 1 || stock == null) {
                    result.setMessage("商品已购完。");
                    result.setSuccess(false);
                }

                //  监视商品
                redisOperations.watch(id);

                //  开启Redis事务
                redisOperations.multi();

                //  命令入列
                // 缓存库的存减 1
                redisTemplate.opsForValue().set(key, (stock - 1));
                // 数据库的库存减 1
                productDAO.reduceStock(id, 1);

                //  执行事务
                List<Object> exec = redisOperations.exec();
                if(exec.size() > 0){
                    result.setSuccess(true);
                    result.setData(true);
                }else{
                    result.setSuccess(false);
                    result.setData(false);
                }
                return exec;
            }
        });
    }
}
