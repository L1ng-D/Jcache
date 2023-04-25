package com.github.houbb.cache.core.bs;

import com.github.houbb.cache.api.ICache;
import com.github.houbb.cache.core.core.Cache;
import com.github.houbb.cache.core.listener.MyRemoveListener;
import com.github.houbb.cache.core.listener.MySlowListener;
import com.github.houbb.cache.core.load.MyCacheLoad;
import com.github.houbb.cache.core.support.evict.CacheEvicts;
import com.github.houbb.cache.core.support.load.CacheLoads;
import com.github.houbb.cache.core.support.map.Maps;
import com.github.houbb.cache.core.support.persist.CachePersists;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Array;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 缓存引导类测试
 *  
 * @since 0.0.2
 */
public class CacheBsTest {

    /**
     * 大小指定测试
     * @since 0.0.2
     */
    @Test
    public void helloTest() {
        ICache<String, String> cache = CacheBs.<String,String>newInstance()
                .size(2)
                .build();

        cache.put("1", "1");
        cache.put("2", "2");
        cache.put("3", "3");
        cache.put("4", "4");

        Assert.assertEquals(2, cache.size());
        System.out.println(cache.keySet());
    }

    /**
     * 配置指定测试
     * @since 0.0.2
     */
    @Test
    public void configTest() {
        ICache<String, String> cache = CacheBs.<String,String>newInstance()
                .map(Maps.<String,String>hashMap())
                .evict(CacheEvicts.<String, String>fifo())
                .size(2)
                .build();

        cache.put("1", "1");
        cache.put("2", "2");
        cache.put("3", "3");
        cache.put("4", "4");

        Assert.assertEquals(2, cache.size());
        System.out.println(cache.keySet());
    }

    /**
     * 过期测试
     * @since 0.0.3
     */
    @Test
    public void expireTest() throws InterruptedException {
        ICache<String, String> cache = CacheBs.<String,String>newInstance()
                .size(3)
                .build();

        cache.put("1", "1");
        cache.put("2", "2");

        cache.expire("1", 40);
        Assert.assertEquals(2, cache.size());

        TimeUnit.MILLISECONDS.sleep(50);
        Assert.assertEquals(1, cache.size());
        System.out.println(cache.keySet());
    }

    /**
     * 缓存删除监听器
     * @since 0.0.6
     */
    @Test
    public void cacheRemoveListenerTest() {
        ICache<String, String> cache = CacheBs.<String,String>newInstance()
                .size(1)
                .addRemoveListener(new MyRemoveListener<String, String>())
                .build();

        cache.put("1", "1");
        cache.put("2", "2");
    }

    /**
     * 加载接口测试
     * @since 0.0.7
     */
    @Test
    public void loadTest() {
        ICache<String, String> cache = CacheBs.<String,String>newInstance()
                .load(new MyCacheLoad())
                .build();

        Assert.assertEquals(2, cache.size());
    }

    /**
     * 持久化接口测试
     * @since 0.0.7
     */
    @Test
    public void persistRdbTest() throws InterruptedException {
        ICache<String, String> cache = CacheBs.<String,String>newInstance()
                .load(new MyCacheLoad())
                .persist(CachePersists.<String, String>dbJson("2.rdb"))
                .build();

//        Assert.assertEquals(2, cache.size());


        TimeUnit.SECONDS.sleep(10);
//        ArrayList<Map.Entry<String, String>> entries = new ArrayList<>(cache.entrySet());
//
//        for (Map.Entry<String, String> entry : entries) {
//            System.out.println(entry.toString());
//        }
    }


    /**
     * 加载接口测试
     * @since 0.0.8
     */
    @Test
    public void loadDbJsonTest() {
        ICache<String, String> cache = CacheBs.<String,String>newInstance()
                .load(CacheLoads.<String, String>dbJson("1.rdb"))
                .build();

        Assert.assertEquals(2, cache.size());
    }

    /**
     * 慢日志接口测试
     * @since 0.0.9
     */
    @Test
    public void slowLogTest() {
        ICache<String, String> cache = CacheBs.<String,String>newInstance()
                .addSlowListener(new MySlowListener())
                .build();

        cache.put("1", "2");
        cache.get("1");
    }


    /**
     * 持久化 AOF 接口测试
     * @since 0.0.10
     */
    @Test
    public void persistAofTest() throws InterruptedException {
        ICache<String, String> cache = CacheBs.<String,String>newInstance()
                .persist(CachePersists.<String, String>aof("1.aof"))
                .build();

        cache.put("1", "1");
        cache.expire("1", 10);
        cache.remove("2");

        TimeUnit.SECONDS.sleep(1);
    }

    /**
     * 加载 AOF 接口测试
     * @since 0.0.10
     */
    @Test
    public void loadAofTest() throws InterruptedException {
        ICache<String, String> cache = CacheBs.<String,String>newInstance()
                .load(CacheLoads.<String, String>aof("1.aof"))
                .persist(CachePersists.<String, String>aof("2.aof"))
                .build();
        cache.put("5", "5");
        TimeUnit.SECONDS.sleep(2);
//        Assert.assertEquals(2, cache.size());
        System.out.println(cache.keySet());
    }


    /**
     * LRU 驱除策略测试
     * @since 0.0.10
     */
    @Test
    public void lruEvictTest() throws InterruptedException {
        ICache<String, HashMap<Integer, Integer>> cache = CacheBs.<String,HashMap<Integer, Integer>>newInstance()
                .size(3)
                .evict(CacheEvicts.<String, HashMap<Integer, Integer>>lru())
                .build();


//a c b
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(1, 2);
        cache.put("A", map);
//        cache.put("B", "world");
//        cache.put("C", "FIFO");
//
//        // 访问一次A
//        cache.containsKey("A");
//        cache.put("C", "LRU");
//        cache.put("D", "LRU");
//        cache.put("F", "LRU");

//        Assert.assertEquals(3, cache.size());
        for (Map.Entry<String, HashMap<Integer, Integer>> entry : cache.entrySet()) {
            System.out.println(entry.getValue());
        }
    }

    @Test
    public void lruDoubleListMapTest() throws InterruptedException {
        ICache<String, String> cache = CacheBs.<String,String>newInstance()
                .size(3)
                .evict(CacheEvicts.<String, String>lruDoubleListMap())
                .build();
        System.out.println("cache = " + System.identityHashCode(cache));
        cache.put("T","O");
        ICache<String, String> cache1 = cache.expire("A",10000);
        ICache<String, String> cache2 = cache1.expire("A",10000);
        System.out.println("cache1 = " + System.identityHashCode(cache1));

        System.out.println(cache2 == cache);

        cache1.put("A", "hello");

//        cache1.put("B", "world");
//        cache1.put("C", "FIFO");
//        cache1.put("C", "FIFO");



//        cache.put("A", "hello");
//        cache.put("B", "world");
//        cache.put("C", "FIFO");
//
//        // 访问一次A
//        cache.get("A");
//        cache.put("D", "LRU");
//        cache.put("E", "LR");
//        cache.put("A", "LR");
//        cache.put("F", "LR");
//        cache.put("G", "LR");
//        cache.remove("G");
//        cache.remove("G");



//        Assert.assertEquals(3, cache.size());
        System.out.println(cache.keySet());
    }

    /**
     * 基于 LinkedHashMap 实现
     * @since 0.0.12
     */
    @Test
    public void lruLinkedHashMapTest()  {
        ICache<String, String> cache = CacheBs.<String,String>newInstance()
                .size(3)
                .evict(CacheEvicts.<String, String>lruLinkedHashMap())
                .build();

        cache.put("A", "hello");
        cache.put("B", "world");
        cache.put("C", "FIFO");

        // 访问一次A
        cache.get("A");
        cache.put("D", "LRU");

        Assert.assertEquals(3, cache.size());
        System.out.println(cache.keySet());
        System.out.println(Integer.parseInt("000"));
    }

    /**
     * 基于 LRU 2Q 实现
     * @since 0.0.13
     */
    @Test
    public void lruQ2Test()  {
        ICache<String, String> cache = CacheBs.<String,String>newInstance()
                .size(3)
                .evict(CacheEvicts.<String, String>lru2Q())
                .build();

        cache.put("A", "hello");
        cache.put("B", "world");
        cache.put("C", "FIFO");

        // 访问一次A
        cache.get("A");
        cache.put("D", "LRU");
        cache.put("A", "LRU");
        cache.put("E", "LRU");

        Assert.assertEquals(3, cache.size());
        System.out.println(cache.keySet());
    }

    /**
     * 基于 LRU-2 实现
     * @since 0.0.13
     */
    @Test
    public void lru2Test()  {
        ICache<String, String> cache = CacheBs.<String,String>newInstance()
                .size(3)
                .evict(CacheEvicts.<String, String>lru2())
                .build();

        cache.put("A", "hello");
        cache.put("B", "world");
        cache.put("C", "FIFO");

        // 访问一次A
        cache.get("A");
        cache.put("D", "LRU");

        Assert.assertEquals(3, cache.size());
        System.out.println(cache.keySet());
    }

    /**
     * 基于 LFU 实现
     * @since 0.0.14
     */
    @Test
    public void lfuTest()  {
        ICache<String, String> cache = CacheBs.<String,String>newInstance()
                .size(3)
                .evict(CacheEvicts.<String, String>lfu())
                .build();

        cache.put("A", "hello");
        cache.put("B", "world");
        cache.put("C", "FIFO");

        // 访问一次A
        cache.get("A");
        cache.put("D", "LRU");

        Assert.assertEquals(3, cache.size());
        System.out.println(cache.keySet());
    }


    /**
     * 基于 clock 算法 实现
     * @since 0.0.15
     */
    @Test
    public void clockTest()  {
        ICache<String, String> cache = CacheBs.<String,String>newInstance()
                .size(3)
                .evict(CacheEvicts.<String, String>clock())
                .build();

        cache.put("A", "hello");
        cache.put("B", "world");
        cache.put("C", "FIFO");

        // 访问一次A
        cache.get("A");
        cache.put("D", "LRU");

        Assert.assertEquals(3, cache.size());
        System.out.println(cache.keySet());
    }

}
