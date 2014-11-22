package ar.com.dgarcia.objectmapper.api.ensemble.cache;

import java.util.function.Supplier;

/**
 * This type represents a cache of weakly referenced objects
 * Created by kfgodel on 21/11/14.
 */
public interface WeakCache<K,V> {

    /**
     * Gets the existing value for the given index or creates a new one and stores it weakly
     * @param index The object used as index
     * @param valueSupplier The code to execute for new values
     * @return The created or existing value
     */
    V getOrCreateFor(K index, Supplier<V> valueSupplier);
}
