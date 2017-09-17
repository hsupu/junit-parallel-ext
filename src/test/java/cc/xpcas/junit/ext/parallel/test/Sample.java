package cc.xpcas.junit.ext.parallel.test;

import java.util.*;
import java.util.concurrent.*;

import org.junit.runner.RunWith;

import cc.xpcas.junit.ext.parallel.*;
import xp.wheel.paralleltester.*;

/**
 * @author xp
 */
@RunWith(ParallelRunner.class)
public class Sample {

    private ConcurrentHashMap<String, Integer> map;

    private Random random = new Random();

    @BeforeCase("test")
    public void setUp() {
        map = new ConcurrentHashMap<>();
    }

    @BeforeThread("test")
    public void beforeThread(String tag, int threadNum) {
        map.putIfAbsent(tag, -1);
    }

    @Threaded(tag = "a", number = 1000)
    //@Threaded(tag = "b", number = 100)
    public void test(String tag, int threadNum) throws InterruptedException {
        map.computeIfPresent(tag, (k, v) -> v + 1);
        Thread.sleep(random.nextInt(100));
    }

    @AfterThread("test")
    public void afterThread(String tag, int threadNum) {
        int value = map.get(tag);
        System.out.println(tag + "[" + threadNum + "] " + value);
    }

    @AfterCase("test")
    public void tearDown() {
        System.out.println("count: " + map.size());
        map = null;
    }

}
