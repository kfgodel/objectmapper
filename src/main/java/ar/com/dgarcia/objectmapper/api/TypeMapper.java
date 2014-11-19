package ar.com.dgarcia.objectmapper.api;

import java.util.Map;

/**
 * This type represents a converter of objects and maps
 * Created by kfgodel on 18/11/14.
 */
public interface TypeMapper {

    Map<String,Object> toMap(Object instance);

    <T> T fromMap(Map<String, Object> map, Class<T> expectedType);
}
