package ar.com.dgarcia.objectmapper.impl.ensemble.disassembly;

import ar.com.dgarcia.objectmapper.api.ensemble.ObjectDisassembler;
import ar.com.dgarcia.objectmapper.api.ensemble.cache.Cache;
import ar.com.dgarcia.objectmapper.api.ensemble.disassembly.DisassemblyTransformer;
import ar.com.dgarcia.objectmapper.impl.ensemble.FieldDisassembler;
import ar.com.dgarcia.objectmapper.impl.ensemble.cache.WeakMapCache;
import ar.com.dgarcia.objectmapper.impl.ensemble.disassembly.transformers.ArrayDisassembler;
import ar.com.dgarcia.objectmapper.impl.ensemble.disassembly.transformers.CollectionDisassembler;
import ar.com.dgarcia.objectmapper.impl.ensemble.disassembly.transformers.EnumDisassembler;
import ar.com.dgarcia.objectmapper.impl.ensemble.disassembly.transformers.MapDisassembler;
import ar.com.dgarcia.objectmapper.impl.ensemble.transformers.NoChange;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * This type represents the converter for values used in maps
 * Created by kfgodel on 20/11/14.
 */
public class DisassembledValueTransformer implements DisassemblyTransformer {

    private Cache<Class<?>, Function<Object, Object>> transformerPerType;
    private Map<Class<?>, Function<Object,Object>> customPerType;

    private ArrayDisassembler arrayDisassembler;
    private CollectionDisassembler collectionDisassembler;
    private MapDisassembler mapDisassembler;
    private ObjectDisassembler objectDisassembler;
    private EnumDisassembler enumDisassembler;

    public static DisassembledValueTransformer create() {
        DisassembledValueTransformer transformer = new DisassembledValueTransformer();
        transformer.transformerPerType = WeakMapCache.create();
        transformer.arrayDisassembler = ArrayDisassembler.create(transformer);
        transformer.collectionDisassembler = CollectionDisassembler.create(transformer);
        transformer.mapDisassembler = MapDisassembler.create(transformer);
        transformer.objectDisassembler = FieldDisassembler.create(transformer);
        transformer.enumDisassembler = EnumDisassembler.create();
        transformer.customPerType = new HashMap<>();
        return transformer;
    }

    @Override
    public Object transform(Object value) {
        if(value == null){
            return null;
        }
        Class<?> valueType = value.getClass();
        Function<Object, Object> typeTransformer = getTransformerFor(valueType);
        Object transformedValue = typeTransformer.apply(value);
        return transformedValue;
    }

    public Function<Object, Object> getTransformerFor(Class<?> valueType) {
        Function<Object, Object> customDefined = customPerType.get(valueType);
        if(customDefined != null){
            return customDefined;
        }
        return transformerPerType.getOrCreateFor(valueType, ()-> defineTransformerFor(valueType));
    }

    @Override
    public <T> void addTransformerFor(Class<T> typicalObjectClass, Function<? super T, ?> customTransformer) {
        customPerType.put(typicalObjectClass, (Function<Object, Object>) customTransformer);
    }

    private Function<Object, Object> defineTransformerFor(Class<?> valueType) {
        if(valueType.isPrimitive()){
            return NoChange.INSTANCE;
        }
        if(Number.class.isAssignableFrom(valueType)){
            return NoChange.INSTANCE;
        }
        if(CharSequence.class.isAssignableFrom(valueType)){
            return NoChange.INSTANCE;
        }
        if(Enum.class.isAssignableFrom(valueType)){
            return enumDisassembler;
        }
        if(valueType.getComponentType() != null){
            return arrayDisassembler;
        }
        if(Collection.class.isAssignableFrom(valueType)){
            return collectionDisassembler;
        }
        if(Map.class.isAssignableFrom(valueType)){
            return mapDisassembler;
        }
        return getObjectDisassembler();
    }

    public ObjectDisassembler getObjectDisassembler() {
        return objectDisassembler;
    }
}
