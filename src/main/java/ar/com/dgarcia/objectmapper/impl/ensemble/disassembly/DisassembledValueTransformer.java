package ar.com.dgarcia.objectmapper.impl.ensemble.disassembly;

import ar.com.dgarcia.objectmapper.api.ensemble.ObjectDisassembler;
import ar.com.dgarcia.objectmapper.api.ensemble.disassembly.DisassemblyTransformer;
import ar.com.dgarcia.objectmapper.impl.ensemble.disassembly.transformers.ArrayDisassembler;
import ar.com.dgarcia.objectmapper.impl.ensemble.disassembly.transformers.CollectionDisassembler;
import ar.com.dgarcia.objectmapper.impl.ensemble.disassembly.transformers.MapDisassembler;
import ar.com.dgarcia.objectmapper.impl.ensemble.disassembly.transformers.NoChange;

import java.util.Collection;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Function;

/**
 * This type represents the converter for values used in maps
 * Created by kfgodel on 20/11/14.
 */
public class DisassembledValueTransformer implements DisassemblyTransformer {

    private Map<Class<?>, Function<Object, Object>> transformerPerType = new WeakHashMap<>();

    private ArrayDisassembler arrayDisassembler;
    private CollectionDisassembler collectionDisassembler;
    private MapDisassembler mapDisassembler;
    private ObjectDisassembler objectDisassembler;

    public static DisassembledValueTransformer create(ObjectDisassembler disassembler) {
        DisassembledValueTransformer transformer = new DisassembledValueTransformer();
        transformer.arrayDisassembler = ArrayDisassembler.create(transformer);
        transformer.collectionDisassembler = CollectionDisassembler.create(transformer);
        transformer.mapDisassembler = MapDisassembler.create(transformer);
        transformer.objectDisassembler = disassembler;
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
        Function<Object, Object> valueTransformer = transformerPerType.get(valueType);
        if(valueTransformer == null){
            valueTransformer = defineTransformerFor(valueType);
            transformerPerType.put(valueType, valueTransformer);
        }
        return valueTransformer;
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
        if(valueType.getComponentType() != null){
            return arrayDisassembler;
        }
        if(Collection.class.isAssignableFrom(valueType)){
            return collectionDisassembler;
        }
        if(Map.class.isAssignableFrom(valueType)){
            return mapDisassembler;
        }
        return objectDisassembler;
    }
}