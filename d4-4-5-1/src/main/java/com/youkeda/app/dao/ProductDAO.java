package com.youkeda.app.dao;

import com.youkeda.app.dataobject.ProductDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductDAO {
    /**
     * 插入商品
     *
     * @param productDO
     * @return
     */
    int insert(ProductDO productDO);

    /**
     * 根据 ID 查询
     *
     * @param id
     * @return
     */
    ProductDO selectById(long id);

    /**
     * 购买商品减少库存
     *
     * @param id    主键
     * @param count 减少的数量
     * @return
     */
    int reduceStock(@Param("id") long id, @Param("count") int count);
}
