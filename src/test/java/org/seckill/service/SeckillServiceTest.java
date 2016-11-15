package org.seckill.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExcution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by SUN on 2016/11/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"
})
public class SeckillServiceTest {

    @Autowired
    private SeckillService seckillService;

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void getSeckillList() throws Exception {
        List<Seckill> list = seckillService.getSeckillList();
        logger.info("list={}",list);
    }

    @Test
    public void getById() throws Exception {
        long id = 1000;
        Seckill seckill = seckillService.getById(id);
        logger.info("seckill={}",seckill);
    }

    @Test
    public void exportSeckillUrl() throws Exception {
        long id = 1000;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        logger.info("exposer={}", exposer);
    }

    //测试代码完整流程，注意可以重复执行
    @Test
    public void execueSeckill() throws Exception {
        long id = 1000;
        long phone = 1234377657;
        String md5 = "cfcb4dc60067fa9e15561f36609ed774";
        try{
            SeckillExcution excution = seckillService.execueSeckill(id,phone,md5);
            logger.info("result={}" ,excution);
        }catch (RepeatKillException e){
            logger.info(e.getMessage());
        }catch (SeckillCloseException e){
            logger.info(e.getMessage());
        }
    }

}