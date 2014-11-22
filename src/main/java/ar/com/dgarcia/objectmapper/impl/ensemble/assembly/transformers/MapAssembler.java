package ar.com.dgarcia.objectmapper.impl.ensemble.assembly.transformers;

import ar.com.dgarcia.objectmapper.api.ensemble.assembly.AssemblyTransformer;
import ar.com.kfgodel.diamond.api.types.TypeInstance;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * This type represents the map assembler taht converts values in the map
 * Created by kfgodel on 21/11/14.
 */
public class MapAssembler implements Function<Object, Object> {

    private AssemblyTransformer valueTransformer;
    private Supplier<Map<String,Object>> mapSupplier;
    private TypeInstance expectedValueType;

    @Override
    public Object apply(Object value) {
        Map<String, Object> asMap = (Map) value;

        Map<String, Object> translatedMap = mapSupplier.get();
        Set<Map.Entry<String, Object>> sourceEntries = asMap.entrySet();
        for (Map.Entry<String, Object> sourceEntry : sourceEntries) {
            Object originalValue = sourceEntry.getValue();
            translatedMap.put(sourceEntry.getKey(), valueTransformer.transform(originalValue, expectedValueType));
        }
        return translatedMap;
    }

    public static MapAssembler create(AssemblyTransformer converter, TypeInstance mapType) {
        MapAssembler assembler = new MapAssembler();
        assembler.valueTransformer = converter;
        assembler.mapSupplier = defineSupplierFor(mapType);
        assembler.expectedValueType = mapType.generics().arguments().skip(1).findFirst().get();
        return assembler;
    }

    private static Supplier<Map<String, Object>> defineSupplierFor(TypeInstance mapType) {
        Class<?> nativeType = mapType.nativeTypes().get();
        if(!nativeType.isInterface()){
            // It's an instantiable concrete type
            return (Supplier)mapType.constructors().niladic().get();
        }
        if(Map.class.equals(nativeType)){
            return LinkedHashMap::new;
        }
        throw new RuntimeException("we don't know how to instantiate: " + nativeType);
    }

}
