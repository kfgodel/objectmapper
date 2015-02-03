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
import ar.com.kfgodel.diamond.api.Diamond;
import ar.com.kfgodel.diamond.api.types.TypeInstance;
import ar.com.kfgodel.diamond.api.types.kinds.Kinds;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * This type represents the converter for values used in maps
 * Created by kfgodel on 20/11/14.
 */
public class DisassembledValueTransformer implements DisassemblyTransformer {

    private Cache<TypeInstance, Function<Object, Object>> transformerPerType;
    private Map<TypeInstance, Function<Object,Object>> customPerType;

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
        TypeInstance valueType = Diamond.of(value.getClass());
        Function<Object, Object> typeTransformer = getTransformerFor(valueType);
        Object transformedValue = typeTransformer.apply(value);
        return transformedValue;
    }

    public Function<Object, Object> getTransformerFor(TypeInstance valueType) {
        Function<Object, Object> customDefined = customPerType.get(valueType);
        if(customDefined != null){
            return customDefined;
        }
        return transformerPerType.getOrCreateFor(valueType, ()-> defineTransformerFor(valueType));
    }

    @Override
    public <T> void addTransformerFor(TypeInstance customType, Function<? super T, ?> customTransformer) {
        customPerType.put(customType, (Function<Object, Object>) customTransformer);
    }

    private Function<Object, Object> defineTransformerFor(TypeInstance valueType) {
        if(valueType.is(Kinds.VALUE)){
            return NoChange.INSTANCE;
        }
        if(valueType.is(Kinds.ENUM)){
            return enumDisassembler;
        }
        if(valueType.is(Kinds.ARRAY)){
            return arrayDisassembler;
        }
        if(valueType.isAssignableTo(Diamond.of(Collection.class))){
            return collectionDisassembler;
        }
        if(valueType.isAssignableTo(Diamond.of(Map.class))){
            return mapDisassembler;
        }
        return getObjectDisassembler();
    }

    public ObjectDisassembler getObjectDisassembler() {
        return objectDisassembler;
    }
}
