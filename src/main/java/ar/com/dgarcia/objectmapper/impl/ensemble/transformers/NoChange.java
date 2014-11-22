package ar.com.dgarcia.objectmapper.impl.ensemble.transformers;

import java.util.function.Function;

/**
 * This type represents the converter for values that need no conversion
 * Created by kfgodel on 20/11/14.
 */
public class NoChange implements Function<Object, Object> {

    public static final NoChange INSTANCE = new NoChange();

    @Override
    public Object apply(Object value) {
        return value;
    }
}
