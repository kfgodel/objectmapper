package ar.com.dgarcia.objectmapper.impl.ensemble.assembly;

import ar.com.dgarcia.objectmapper.api.ensemble.ObjectAssembler;
import ar.com.dgarcia.objectmapper.api.ensemble.assembly.AssemblyTransformer;
import ar.com.dgarcia.objectmapper.api.ensemble.cache.Cache;
import ar.com.dgarcia.objectmapper.impl.ensemble.FieldAssembler;
import ar.com.dgarcia.objectmapper.impl.ensemble.assembly.transformers.*;
import ar.com.dgarcia.objectmapper.impl.ensemble.cache.WeakMapCache;
import ar.com.dgarcia.objectmapper.impl.ensemble.transformers.NoChange;
import ar.com.kfgodel.diamond.api.Diamond;
import ar.com.kfgodel.diamond.api.types.TypeInstance;
import ar.com.kfgodel.diamond.api.types.kinds.Kinds;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * This type represents the value transformer for assembled values
 * Created by kfgodel on 21/11/14.
 */
public class AssembledValueTransformer implements AssemblyTransformer {

    private Cache<TypeInstance, Function<Object, Object>> transformerPerType;
    private Map<TypeInstance, Function<Object,Object>> customPerType;

    private ObjectAssembler objectAssembler;

    public static AssembledValueTransformer create() {
        AssembledValueTransformer transformer = new AssembledValueTransformer();
        transformer.transformerPerType = WeakMapCache.create();
        transformer.customPerType = new HashMap<>();
        transformer.objectAssembler = FieldAssembler.create(transformer);
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

    @Override
    public <T> void addTransformerFor(TypeInstance typicalObjectClass, Function<?, ? extends T> customTransformer) {
        customPerType.put(typicalObjectClass, (Function<Object, Object>) customTransformer);
    }

    public Function<Object, Object> getTransformerFor(TypeInstance valueType) {
        Function<Object, Object> customDefined = customPerType.get(valueType);
        if(customDefined != null){
            return customDefined;
        }
        return transformerPerType.getOrCreateFor(valueType, ()-> defineTransformerFor(valueType));
    }

    private Function<Object, Object> defineTransformerFor(TypeInstance expectedType) {
        if(expectedType.is(Kinds.VALUE)){
            return NoChange.INSTANCE;
        }
        if(expectedType.is(Kinds.ENUM)){
            return EnumAssembler.create((Class<? extends Enum>) expectedType.nativeTypes().get());
        }
        if(expectedType.is(Kinds.ARRAY)){
            return ArrayAssembler.create(this, expectedType);
        }
        if(expectedType.isAssignableTo(Diamond.of(Collection.class))){
            return CollectionAssembler.create(this, expectedType);
        }
        if(expectedType.isAssignableTo(Diamond.of(Map.class))){
            return MapAssembler.create(this, expectedType);
        }
        return ObjectAssemblerAdapter.create(objectAssembler, expectedType);
    }

}
