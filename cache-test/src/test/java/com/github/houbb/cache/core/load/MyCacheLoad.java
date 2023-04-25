package com.github.houbb.cache.core.load;

import com.github.houbb.cache.api.ICache;
import com.github.houbb.cache.api.ICacheLoad;

/**
 *  
 * @since 0.0.7
 */
public class MyCacheLoad implements ICacheLoad<String,String> {

    @Override
    public void load(ICache<String, String> cache) {
        cache.put("1", "1");
        cache.put("2", "2");
        cache.put("3", "3");
        cache.expire("1", 1000);
        cache.expire("3", 1000);
        cache.expire("2", 2000);
    }

}
