package org.seckill.web;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExcution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.SecKill;
import org.seckill.enums.SeckillStateEnums;
import org.seckill.exception.RepeatException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by junbiao on 2018/7/16.
 */

/**
 * 注意：restful接口风格：模块/资源/集合/操作
 *    seckill|{seckillId}\delete
 */
@Controller
@RequestMapping("/seckill")  //项目映射模块
public class SeckillController {

    private static final Logger logger = LoggerFactory.getLogger(SeckillController.class);
    @Autowired
    private SeckillService seckillService;

    /**
     * 获取商品列表页面
     * @param model  用于保存数据，返回给页面
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public String list(Model model){
        List<SecKill> secKillList = seckillService.getAll();
        model.addAttribute("seckilList",secKillList);
        return "list";
    }

    /**
     * 商品详情页
     * @param seckillId  商品id
     * @param model  用于保存数据，返回给页面
     * @return
     */
    @RequestMapping(value = "/{seckillId}/detail",method = RequestMethod.GET)
    public String detail(@PathVariable String seckillId,Model model){
        SecKill secKill = seckillService.qetById(Integer.parseInt(seckillId));
        model.addAttribute("seckill",secKill);
        return "detail";
    }

    /**
     * 暴露秒杀接口
     * @param seckillId  商品id
     * @return
     */
    @RequestMapping(value = "/{seckillId}/exposer",
            produces = {"application/json;charset=utf-8"})
    //把返回数据转换成JSON格式，绑定到响应中
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable Integer seckillId){
        SeckillResult<Exposer> result;
        try {
            Exposer exposer = seckillService.exposeSeckillUrl(seckillId);
            result = new SeckillResult<Exposer>(true,exposer);
        }catch (Exception ex){
            result = new SeckillResult<Exposer>(false,ex.getMessage());
            logger.error(ex.getMessage());
        }
        return  result;
    }

    /**
     * 执行秒杀
     * @param seckillId  商品id
     * @param md5        md5加密
     * @param killPhone  用户秒杀的手机号
     * @return
     */
    @RequestMapping(value = "/{seckillId}/{md5}/execution",
            produces = {"application/json;charset=utf-8"}
    )
    @ResponseBody
    public SeckillResult<SeckillExcution> execute(@PathVariable Integer seckillId,
                                                @PathVariable String md5,
                                                @CookieValue(value = "killPhone",required = false)String killPhone){
     if (killPhone == null){
         return new SeckillResult<SeckillExcution>(false,"未注册");
     }
     try {
         //调用存储过程
        // SeckillExcution excution = seckillService.executeSeckill(seckillId,killPhone,md5);
         SeckillExcution excution = seckillService.executeSeckillByProcedure(seckillId,killPhone,md5);
         return new SeckillResult<SeckillExcution>(true,excution);
     }catch (RepeatException e){
         SeckillExcution excution = new SeckillExcution(SeckillStateEnums.REPEAT_KILL,seckillId);
         return new SeckillResult<SeckillExcution>(true,excution);
     }catch (SeckillException e){
          SeckillExcution excution = new SeckillExcution(SeckillStateEnums.END,seckillId);
          return new SeckillResult<SeckillExcution>(true,excution);
     }catch (Exception e){
         SeckillExcution excution = new SeckillExcution(SeckillStateEnums.DATA_REWRITE,seckillId);
         return new SeckillResult<SeckillExcution>(true,excution);
     }
    }

    /**
     * 获取系统时间
     * @return
     */
    @RequestMapping(value = "/time/now",method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time(){
        Date now = new Date();
        return new SeckillResult<Long>(true,now.getTime());
    }
}
