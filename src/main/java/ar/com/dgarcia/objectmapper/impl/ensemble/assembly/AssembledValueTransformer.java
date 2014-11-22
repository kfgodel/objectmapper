package ar.com.dgarcia.objectmapper.impl.ensemble.assembly;

import ar.com.dgarcia.objectmapper.api.ensemble.ObjectAssembler;
import ar.com.dgarcia.objectmapper.api.ensemble.assembly.AssemblyTransformer;
import ar.com.dgarcia.objectmapper.api.ensemble.cache.WeakCache;
import ar.com.dgarcia.objectmapper.impl.ensemble.assembly.transformers.ArrayAssembler;
import ar.com.dgarcia.objectmapper.impl.ensemble.assembly.transformers.CollectionAssembler;
import ar.com.dgarcia.objectmapper.impl.ensemble.assembly.transformers.MapAssembler;
import ar.com.dgarcia.objectmapper.impl.ensemble.assembly.transformers.ObjectAssemblerAdapter;
import ar.com.dgarcia.objectmapper.impl.ensemble.cache.WeakMapCache;
import ar.com.dgarcia.objectmapper.impl.ensemble.transformers.NoChange;
import ar.com.kfgodel.diamond.api.types.TypeInstance;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

/**
 * This type represents the value transformer for assembled values
 * Created by kfgodel on 21/11/14.
 */
public class AssembledValueTransformer implements AssemblyTransformer {

    private WeakCache<TypeInstance, Function<Object, Object>> transformerPerType;
    private ObjectAssembler assembler;

    public static AssembledValueTransformer create(ObjectAssembler assembler) {
        AssembledValueTransformer transformer = new AssembledValueTransformer();
        transformer.transformerPerType = WeakMapCache.create();
        transformer.assembler = assembler;
        return transformer;
    }

    @Override
    public Object transform(Object value, TypeInstance expectedType) {
        if(value == null){
            return null;
        }
        Function<Object, Object> typeTransformer = getTransformerFor(expectedType);
        Object transformedValue = typeTransformer.apply(value);
        return transformedValue;
    }

    public Function<Object, Object> getTransformerFor(TypeInstance valueType) {
        return transformerPerType.getOrCreateFor(valueType, ()-> defineTransformerFor(valueType));
    }

    private Function<Object, Object> defineTransformerFor(TypeInstance expectedType) {
        Class<?> nativeType = expectedType.nativeTypes().get();
        if(nativeType.isPrimitive()){
            return NoChange.INSTANCE;
        }
        if(Number.class.isAssignableFrom(nativeType)){
            return NoChange.INSTANCE;
        }
        if(CharSequence.class.isAssignableFrom(nativeType)){
            return NoChange.INSTANCE;
        }
        Class<?> arrayComponentType = nativeType.getComponentType();
        if(arrayComponentType != null){
            return ArrayAssembler.create(this, expectedType);
        }
        if(Collection.class.isAssignableFrom(nativeType)){
            return CollectionAssembler.create(this, expectedType);
        }
        if(Map.class.isAssignableFrom(nativeType)){
            return MapAssembler.create(this, expectedType);
        }
        return ObjectAssemblerAdapter.create(assembler, expectedType);
    }

}
