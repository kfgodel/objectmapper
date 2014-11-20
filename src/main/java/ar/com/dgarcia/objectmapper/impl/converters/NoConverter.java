package ar.com.dgarcia.objectmapper.impl.converters;

import java.util.function.Function;

/**
 * This type represents the converter for values that need no conversion
 * Created by kfgodel on 20/11/14.
 */
public class NoConverter implements Function<Object, Object> {

    public static final NoConverter INSTANCE = new NoConverter();

    @Override
    public Object apply(Object value) {
        return value;
    }
}
