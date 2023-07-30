package wuhan.science.server.config;

import cn.hutool.db.nosql.redis.RedisDS;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    Jedis jedis = RedisDS.create().getJedis();
    @Override
    public void onMessage(Message message, byte[] pattern) {
        // 过期key
        String expiredKey = message.toString();
        System.out.println(expiredKey + "过期了");
        // 下面可以通过redis工具获取值或者执行业务逻辑
        jedis.incr(expiredKey.split("-")[1]);
        System.out.println(jedis.get(expiredKey.split("-")[1]));
    }

    public RedisKeyExpirationListener(RedisMessageListenerContainer redisMessageListenerContainer) {
        super(redisMessageListenerContainer);
    }
}