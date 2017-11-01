package com.mytaotao.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@Service
public class RedisService {

    @Autowired(required = false)
    private ShardedJedisPool shardedJedisPool;
    
    /**  
     * @Description: 从连接池中获取一个jedis分区对象ShardedJedis
     *               并对其进行操作,
     *               具体操作交个一个实现了RedisStrategy的类的handle方法
     * @Author: kuang 
     * @Parameters: 
     * @Return: 
     */
    private <T> T execute(RedisStrategy<ShardedJedis, T> strategy){
        ShardedJedis shardedJedis=null;
        try{
            //从连接池中获取jedis分片对象
            shardedJedis = shardedJedisPool.getResource();
            return strategy.handle(shardedJedis);
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != shardedJedis) {
                // 关闭，检测连接是否有效，有效则放回到连接池中，无效则重置状态
                shardedJedis.close();
            }
        }
        return null;
    }

    /**  
     * @Description: 设置键和值
     * @Author: kuang 
     * @Parameters: 
     * @Return: 
     */
    public String set(final String key,final String value){
        return this.execute(new RedisStrategy<ShardedJedis,String>(){
            @Override
            public String handle(ShardedJedis jedis) {
                return jedis.set(key, value);
            }
        });
    }
    
    /**  
     * @Description: 设置键和值,并设置键存在的事间(秒)
     * @Author: kuang 
     * @Parameters: 
     * @Return: 
     */
    public String set(final String key,final String value,final int seconds){
        return this.execute(new RedisStrategy<ShardedJedis,String>(){
            @Override
            public String handle(ShardedJedis jedis) {
                String retValue=jedis.set(key,value);
                jedis.expire(key, seconds);
                return retValue;
            }
        });
    }
    
    
    /**  
     * @Description: 通过键获取值
     * @Author: kuang 
     * @Parameters: 
     * @Return: 
     */
    public String get(final String key){
        return this.execute(new RedisStrategy<ShardedJedis, String>() {
            @Override
            public String handle(ShardedJedis jedis) {
                return jedis.get(key);
            }
            
        });
    }
    
    /**  
     * @Description: 设置键存在的事件(秒)
     * @Author: kuang 
     * @Parameters: 
     * @Return: 
     */
    public Long setKey(final String key,final int seconds){
        return this.execute(new RedisStrategy<ShardedJedis, Long>() {
            @Override
            public Long handle(ShardedJedis jedis) {
                return jedis.expire(key, seconds);
            }
        });
    }
    
    /**  
     * @Description: 删除键
     * @Author: kuang 
     * @Parameters: 
     * @Return: 
     */
    public Long delete(final String key){
        return this.execute(new RedisStrategy<ShardedJedis, Long>() {
            @Override
            public Long handle(ShardedJedis jedis) {
                return jedis.del(key);
            }
        });
    }

}
