package org.seckill.service.impl;

import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExcution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * @Component：所有组件
 * @Service
 * @Dao
 * @Controller
 * Created by SUN on 2016/11/10.
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    //注入service依赖
    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private SuccessKilledDao successKilledDao;

    private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    //md5字符串，用于混淆MD5
    private final String slat = "sjdfakljkjsl;kdjf23r9jiofjadfnncx^&(*&8sdf";

    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0,4);
    }

    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    public Exposer exportSeckillUrl(long seckillId) {
        Seckill seckill = seckillDao.queryById(seckillId);
        if(seckill == null){
            return new Exposer(false,seckillId);
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();
        if(nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()){
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }
        //转化特定字符串的过程，不可逆
        String md5 = getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    private String getMD5(long seckillId){
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    @Transactional
    /*
    使用注解控制事务方法优点：
    1、开发团队达成一致约定，明确标注事务编程方法的编程风格
    2、保证事务方法的执行时间尽可能短，不要穿插其他网络操作请求，或者剥离事务到外部方法
    3、不是所有的方法都需要事务
     */
    public SeckillExcution execueSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        if(md5 == null || !md5.equals(getMD5(seckillId))){
            throw new SeckillException("seckill data rewrite!");
        }
        try{
            //执行秒杀逻辑：减库存+记录购买行为
            int updateCount = seckillDao.reduceNumber(seckillId,new Date());
            if(updateCount <= 0){
                //没有更新到记录
                throw new SeckillCloseException("scekill is closed");
            }else {
                //减库存成功记录购买行为
                int insertCount = successKilledDao.insertSuccessKilled(seckillId,userPhone);
                if(insertCount <= 0){
                    //重复秒杀
                    throw  new RepeatKillException("seckill repeated");
                }else {
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
                    return new SeckillExcution(seckillId, SeckillStatEnum.SUCCESS,successKilled);
                }
            }
        }catch (SeckillCloseException e1){
            throw e1;
        }catch (RepeatKillException e2){
            throw e2;
        }catch (Exception e3){
            logger.error(e3.getMessage(),e3);
            //所有编译期异常，转化为运行期异常
            throw  new SeckillException("seckill inner error" + e3.getMessage());
        }
    }
}
