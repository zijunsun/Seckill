package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by SUN on 2016/11/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring 配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})

public class SuccessKilledDaoTest {
    @Resource
    private SuccessKilledDao successKilledDao;

    @Test
    public void insertSuccessKilled() throws Exception {
        successKilledDao.insertSuccessKilled(1002,1234567);
    }

    @Test
    public void queryByIdWithSeckill() throws Exception {
        SuccessKilled successKilled =  successKilledDao.queryByIdWithSeckill(1002,1234567);
        System.out.println(successKilled);
        System.out.println(successKilled.getSeckill());
    }

}