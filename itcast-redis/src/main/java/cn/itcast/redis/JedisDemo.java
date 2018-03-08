package cn.itcast.redis;

import redis.clients.jedis.Jedis;

/**
 * 简单redis使用
 * @author srhsw95_Administrator
 * @email  srhsw95@163.com
 * @version 2017年2月11日 下午3:15:50
 * we can not wait,we can not stop!
 */
public class JedisDemo {

    public static void main(String[] args) {
        // 构造jedis对象
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        // 向redis中添加数据
        jedis.set("mytest", "123");
        // 从redis中读取数据
        String value = jedis.get("mytest");

        System.out.println(value);
        // 关闭连接
        jedis.close();

    }

}
