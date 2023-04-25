package com.github.houbb.cache.core.support.interceptor.evict;

import com.github.houbb.cache.api.*;
import com.github.houbb.cache.core.constant.enums.CacheRemoveType;
import com.github.houbb.cache.core.support.evict.CacheEvictContext;
import com.github.houbb.cache.core.support.listener.remove.CacheRemoveListenerContext;
import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

import java.lang.reflect.Method;

/**
 * 驱除策略拦截器
 *
 *  
 * @since 0.0.11
 */
public class CacheInterceptorEvict<K,V> implements ICacheInterceptor<K, V> {

    private static final Log log = LogFactory.getLog(CacheInterceptorEvict.class);

    @Override
    public void before(ICacheInterceptorContext<K,V> context) {
        ICacheEvict<K,V> evict = context.cache().evict();

        Method method = context.method();
        final K key = (K) context.params()[0];
        if("remove".equals(method.getName())) {
            evict.removeKey(key);
        } else if ("put".equals(method.getName())){
            evict.updateKey(key);

            CacheEvictContext<K,V> context1 = new CacheEvictContext<>();
            ICache<K, V> cache = context.cache();
            context1.key(key).size(cache.sizeLimit()).cache(cache);

            if (!cache.containsKey(key)){

                ICacheEntry<K, V> evictEntry = evict.evict(context1);

                if(ObjectUtil.isNotNull(evictEntry)) {
                    // 执行淘汰监听器
                    ICacheRemoveListenerContext<K,V> removeListenerContext = CacheRemoveListenerContext.<K,V>newInstance().key(evictEntry.key())
                            .value(evictEntry.value())
                            .type(CacheRemoveType.EVICT.code());
                    for(ICacheRemoveListener<K,V> listener : context.cache().removeListeners()) {
                        listener.listen(removeListenerContext);
                    }
                }

            }
        }else{
            evict.updateKey(key);
        }




    }

    @Override
    @SuppressWarnings("all")
    public void after(ICacheInterceptorContext<K,V> context) {
//        ICacheEvict<K,V> evict = context.cache().evict();
//
//        Method method = context.method();
//        final K key = (K) context.params()[0];
//        if("remove".equals(method.getName())) {
//            evict.removeKey(key);
//        } else {
//            evict.updateKey(key);
//        }
    }

}
