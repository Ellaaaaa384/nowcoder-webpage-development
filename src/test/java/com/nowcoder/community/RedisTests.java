package com.nowcoder.community;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;

import java.util.concurrent.TimeUnit;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class RedisTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testStrings() {
        String redisKey = "test:count";
        redisTemplate.opsForValue().set(redisKey,1);

        System.out.println(redisTemplate.opsForValue().get(redisKey));
        System.out.println(redisTemplate.opsForValue().increment(redisKey));
        System.out.println(redisTemplate.opsForValue().decrement(redisKey));
    }

    @Test
    public void testHashes() {
        String redisKey = "test:user";
        redisTemplate.opsForHash().put(redisKey,"id","1");
        redisTemplate.opsForHash().put(redisKey,"username","zhangsan");

        System.out.println(redisTemplate.opsForHash().get(redisKey,"id"));
        System.out.println(redisTemplate.opsForHash().get(redisKey,"username"));
    }

    @Test
    public void testLists() {
        String redisKey = "test:ids";
        redisTemplate.opsForList().leftPush(redisKey,101);
        redisTemplate.opsForList().leftPush(redisKey,102);
        redisTemplate.opsForList().leftPush(redisKey,103);

        System.out.println(redisTemplate.opsForList().size(redisKey));
        System.out.println(redisTemplate.opsForList().index(redisKey,0));
        System.out.println(redisTemplate.opsForList().range(redisKey,0,2));

        System.out.println(redisTemplate.opsForList().leftPop(redisKey));
        System.out.println(redisTemplate.opsForList().rightPop(redisKey));
        System.out.println(redisTemplate.opsForList().leftPop(redisKey));
    }

    @Test
    public void testSets() {
        String redisKey = "test:teachers";
        redisTemplate.opsForSet().add(redisKey,"aaa", "bbb", "ccc", "ddd", "eee");

        System.out.println(redisTemplate.opsForSet().size(redisKey));
        System.out.println(redisTemplate.opsForSet().pop(redisKey)); // 随机弹出
        System.out.println(redisTemplate.opsForSet().members(redisKey)); // 看看还剩什么values
    }

    @Test
    public void testSortedSets() {
        String redisKey = "test:students";
        redisTemplate.opsForZSet().add(redisKey,"a",50);
        redisTemplate.opsForZSet().add(redisKey,"b",40);
        redisTemplate.opsForZSet().add(redisKey,"c",30);
        redisTemplate.opsForZSet().add(redisKey,"d",20);
        redisTemplate.opsForZSet().add(redisKey,"e",10);

        System.out.println(redisTemplate.opsForZSet().zCard(redisKey));
        System.out.println(redisTemplate.opsForZSet().score(redisKey,"b"));
        System.out.println(redisTemplate.opsForZSet().rank(redisKey,"c")); // 默认由小到大
        System.out.println(redisTemplate.opsForZSet().reverseRank(redisKey,"d")); // 由大到小倒序
        System.out.println(redisTemplate.opsForZSet().range(redisKey, 0, 2)); // 由小到大取前三名
        System.out.println(redisTemplate.opsForZSet().reverseRange(redisKey, 0, 2)); // 由大到小取前三名
    }

    @Test
    public void testKeys() {
        redisTemplate.delete("test:user");
        System.out.println(redisTemplate.hasKey("test:user"));
        redisTemplate.expire("test:students", 10, TimeUnit.SECONDS);
    }

}
