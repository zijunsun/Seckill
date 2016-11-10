package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

import java.util.Date;
import java.util.List;

/**
 * Created by SUN on 2016/11/1.
 */
public interface SeckillDao {
    //减库存
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

    //根据id查询秒杀对象
    Seckill queryById(long seckillId);

    //根据偏移量查询秒杀商品
    List<Seckill> queryAll(@Param("offset") int offset, @Param("limit")int limit);

}
