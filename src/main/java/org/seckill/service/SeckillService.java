package org.seckill.service;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExcution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by SUN on 2016/11/10.
 */
public interface SeckillService {

    //查询所有秒杀记录
    List<Seckill> getSeckillList();

    //查询单个秒杀记录
    Seckill getById(long seckillId);

    //输出秒杀接口的地址
    //秒杀开启时输出秒杀接口地址，否则输出系统时间和秒杀时间
    Exposer exportSeckillUrl(long seckillId);

    //执行秒杀操作
    SeckillExcution execueSeckill(long seckill, long userPhone, String md5)
            throws SeckillException,RepeatKillException,SeckillCloseException;


}
