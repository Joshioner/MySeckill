package org.seckill.dao.cache;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.runtime.RuntimeSchema;
import org.seckill.entity.SecKill;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by junbiao on 2018/7/18.
 */
public class RedisDao {

    private  JedisPool jedisPool;
    //protostuff通过类的schema来进行序列化，所以传入要序列化的类创建一个自定义的schema。
    private RuntimeSchema<SecKill> schema = RuntimeSchema.createFrom(SecKill.class);

    //创造构建函数，需要主机号，端口作为参数
    public RedisDao(String ip,int port){
        jedisPool = new JedisPool(ip,port);
    }

    /**
     * 从缓存中获取数据，有的话直接返回
     * @param seckillId
     * @return
     */
    public SecKill getSeckill(Integer seckillId){
     try {
         Jedis jedis = jedisPool.getResource();
        try {
            //键名
            String key = "seckillId:" + seckillId;
            //需要通过反序列化拿到数据
            //get byte[] ---->反序列化得到seckill
            //这里采用第三方的自定义序列化工具类protostuff
            //通过key来查询对象，对象在cache中以序列化形式存在，所以返回的是字节数组
            byte[] bytes = jedis.get(key.getBytes());
            if (bytes != null){
                //创建一个对象，用于接收转换后的对象
                SecKill secKill = schema.newMessage();
                //把字节数组根据schema转换成对象，传到空对象中——反序列化
                ProtostuffIOUtil.mergeFrom(bytes,secKill,schema);
                return secKill;
            }
        }finally {
            jedis.close();
        }
     }catch (Exception ex){
         ex.printStackTrace();
     }
     return null;
    }

    /**
     * 将对象进行序列化后，放入redis缓存中
     * @param seckill
     * @return
     */
   public String putSeckill(SecKill seckill){
      try {
          Jedis jedis = jedisPool.getResource();
          try {
              String key = "seckillId:" + seckill.getSeckillId();
              //将对象序列化为字节数组
            byte[] bytes = ProtostuffIOUtil.toByteArray(seckill,schema,
                    LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
            int timeOut = 60 * 60;
            //将字节数组保存到缓存中
            String result = jedis.setex(key.getBytes(),timeOut,bytes);
            //执行成功，返回结果
            return result;
          }finally {
              jedis.close();
          }
      }catch (Exception ex){
          ex.printStackTrace();
      }
      return null;
   }
}
