package com.coy.l2cache.cache;

import com.coy.l2cache.cache.sync.CacheSyncPolicy;
import com.coy.l2cache.consts.CacheConsts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.function.Function;

/**
 * 加载数据 Function
 * <p>
 * LoadFunction封装了L2的数据加载逻辑，主要是为了支持如下获取数据的场景：
 * Caffeine.get(key, Function)
 * Guava.get(key, Function)
 * ConcurrentHashMap.computeIfAbsent(key, Function)
 *
 * @author chenck
 * @date 2020/5/15 12:37
 */
public class LoadFunction implements Function<Object, Object> {

    private static final Logger logger = LoggerFactory.getLogger(LoadFunction.class);

    private final Cache level2Cache;
    private final CacheSyncPolicy cacheSyncPolicy;
    private final Callable<?> valueLoader;

    public LoadFunction(Cache level2Cache, CacheSyncPolicy cacheSyncPolicy, Callable<?> valueLoader) {
        this.level2Cache = level2Cache;
        this.cacheSyncPolicy = cacheSyncPolicy;
        this.valueLoader = valueLoader;
    }

    @Override
    public Object apply(Object key) {
        try {
            Object value = null;
            if (null == level2Cache) {
                value = valueLoader.call();
                logger.debug("[LoadFunction] load data from method, level2Cache is null, cacheName={}, key={}, value={}", level2Cache.getName(),
                        key, value);
            } else {
                logger.debug("[LoadFunction] load cache, cacheName={}, key={}", level2Cache.getName(), key);
                // 走到此处，表明已经从L1中没有获取到数据，所以先从L2中获取数据
                value = level2Cache.get(key);

                if (value != null) {
                    logger.debug("[LoadFunction] get cache from redis, cacheName={}, key={}, value={}", level2Cache.getName(), key, value);
                    // 从L2中获取到数据后不需要显示设置到L1，利用L1本身的机制进行设置
                    return value;
                }

                // 执行业务方法获取数据
                value = valueLoader.call();
                logger.debug("[LoadFunction] load data from method, cacheName={}, key={}, value={}", level2Cache.getName(), key, value);

                level2Cache.put(key, value);
            }
            if (null != cacheSyncPolicy) {
                cacheSyncPolicy.publish(key, CacheConsts.CACHE_REFRESH);
            }
            return value;
        } catch (Exception ex) {
            throw new org.springframework.cache.Cache.ValueRetrievalException(key, this.valueLoader, ex);
        }
    }
}