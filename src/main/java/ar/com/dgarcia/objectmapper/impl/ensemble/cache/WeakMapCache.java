package ar.com.dgarcia.objectmapper.impl.ensemble.cache;

import ar.com.dgarcia.objectmapper.api.ensemble.cache.WeakCache;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Supplier;

/**
 * This type implements a weak cache with a weakHashMap
 * Created by kfgodel on 21/11/14.
 */
public class WeakMapCache<K,V> implements WeakCache<K,V> {

    private Map<K, V> cacheMap;

    @Override
    public V getOrCreateFor(K index, Supplier<V> valueSupplier) {
        V existingValue = cacheMap.get(index);
        if(existingValue != null){
            return existingValue;
        }
        V createdValue = valueSupplier.get();
        cacheMap.put(index, createdValue);
        return createdValue;
    }

    public static<K,V> WeakMapCache<K,V> create() {
        WeakMapCache<K,V> cache = new WeakMapCache<>();
        cache.cacheMap = new WeakHashMap<>();
        return cache;
    }

}
