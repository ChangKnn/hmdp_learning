package com.hmdp;

import com.hmdp.service.impl.ShopServiceImpl;
import com.hmdp.utils.RedisIdWorker;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class HmDianPingApplicationTests {
    @Resource
    private RedisIdWorker redisIdWorker;
    @Resource
    private ShopServiceImpl shopService;
    @Test
    public void saveShop2RedisTest() throws InterruptedException {
        shopService.saveShop2Redis(1,2);
    }

    private ExecutorService ex = Executors.newFixedThreadPool(500);
    @Test
    public void idTest() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(500);
        Runnable task = () -> {
            for (int i = 0; i < 300; i++) {
                long idGenerator = redisIdWorker.idGenerator("test");
                System.out.println("id:" + idGenerator);
            }
            countDownLatch.countDown();
        };
        for (int i = 0; i < 500; i++) {
            ex.submit(task);
        }
        countDownLatch.await();
    }
    @Test
    public void idTest1() {
        System.out.println(redisIdWorker.idGenerator("test"));
    }
}
