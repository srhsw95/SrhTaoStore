package com.taotao.common.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.interfaces.Function;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;


/**
 * @author Srhsw95
 * @version 2017年2月11日 下午3:49:20
 */
@Service
public class RedisService {
    @Autowired(required=false)
    private ShardedJedisPool shardedJedisPool;

    /**
     * 抽取出来的相同代码 涉及到：泛型 内部类 等等 方法一级的泛型定义 这边的T
     * 
     * @param fun
     * @return 下面 这个<T> 方法一级的泛型定义
     */
    private <T> T excute(Function<T, ShardedJedis> fun) {
        ShardedJedis shardedJedis = null;
        try {
            // 从连接池中获取到jedis分片对象
            shardedJedis = shardedJedisPool.getResource();
            // 设置数据
            return fun.callback(shardedJedis);
        } finally {
            if (null != shardedJedis) {
                // 关闭，检测连接是否有效，有效则放回到连接池中，无效则重置状态
                shardedJedis.close();
            }
        }
    }

    /**
     * Redis set操作
     * 
     * @param key
     * @param value
     * @return
     */
    public String set(final String key, final String value) {
        return this.excute(new Function<String, ShardedJedis>() {
            @Override
            public String callback(ShardedJedis e) {
                return e.set(key, value);
            }

        });
    }

    /**
     * redis set操作
     * 
     * @param key
     * @return
     */
    public String get(final String key) {
        return this.excute(new Function<String, ShardedJedis>() {
            @Override
            public String callback(ShardedJedis e) {
                return e.get(key);
            }
        });
    }

    /**
     * redis 删除操作
     */
    public Long del(final String key) {
        // new Function<返回参数，传入参数>
        return this.excute(new Function<Long, ShardedJedis>() {
            @Override
            public Long callback(ShardedJedis e) {
                // TODO Auto-generated method stub
                return e.del(key);
            }
        });
    }

    /**
     * 设置生存时间
     * 
     * @param key
     * @return
     */
    public Long expire(final String key, final Integer seconds) {
        // new Function<返回参数，传入参数>
        return this.excute(new Function<Long, ShardedJedis>() {
            @Override
            public Long callback(ShardedJedis e) {
                // TODO Auto-generated method stub
                return e.expire(key, seconds);
            }
        });
    }

    /**
     * redis set操作 同时进行设置生存时间
     * 
     * @param key
     * @param value 
     * @param seconds 生存时间 秒
     * @return
     */
    public String set(final String key, final String value, final Integer seconds) {
        return this.excute(new Function<String, ShardedJedis>() {
            @Override
            public String callback(ShardedJedis e) {
                String str = e.set(key, value);
                e.expire(key, seconds);
                return str;
            }
        });
    }
    
    /**
     * Hash结构获取值
     * @param key  cookie值 未登录用户标记信息
     * @param field  购物车商品id
     * @return  此商品对应的购物车信息
     */
    public String hget(final String key,final String field) {
        // TODO Auto-generated method stub
        return this.excute(new Function<String,ShardedJedis>(){
            @Override
            public String callback(ShardedJedis e) {

                return e.hget(key, field);
            }
        });
    }
    
    /**
     * Hash结构设置值
     * @param key  cookie值 未登录用户标记信息
     * @param field  购物车商品id
     * @param value  此商品对应的购物车信息
     * @return
     */
    public Long hset(final String key,final String field,final String value) {
        // TODO Auto-generated method stub
        return this.excute(new Function<Long,ShardedJedis>(){
            @Override
            public Long callback(ShardedJedis e) {
                return e.hset(key, field, value);
            }
        });
    }
    
    /**
     * Hash结构设置值-设置生存时间
     * @param key   cookie值
     * @param field 商品id
     * @param value 此商品对应的购物车信息
     * @param seconds  生存时间
     * @return  执行数量
     */
    public Long hset(final String key,final String field,final String value,final Integer seconds) {
        // TODO Auto-generated method stub
        return this.excute(new Function<Long,ShardedJedis>(){
            @Override
            public Long callback(ShardedJedis e) {
                Long count = e.hset(key, field, value);
                e.expire(key, seconds);
                return count;
            }
        });
    }
    
    
    
    /**
     * 删除 Hash结构  可删除多个
     * @param key   cookie值
     * @param fields 商品Id
     * @return
     */
    public Long hdel(final String key,final String ... fields) {
        // TODO Auto-generated method stub
        return this.excute(new Function<Long,ShardedJedis>(){
            @Override
            public Long callback(ShardedJedis e) {
                return e.hdel(key, fields);
            }
        });
    }
    
   /**
    * 获取此未登录用户所有购物车信息
    * @param key cookie值（当前未登录用户标记）
    * @return
    */
    public Map<String,String> hgetAll(final String key) {
        // TODO Auto-generated method stub
        return this.excute(new Function<Map<String,String>,ShardedJedis>(){
            @Override
            public Map<String,String> callback(ShardedJedis e) {
                return e.hgetAll(key);
            }
        });
    }
}
