package ar.com.dgarcia.objectmapper.impl.ensemble.disassembly.transformers;

import ar.com.dgarcia.objectmapper.api.ensemble.disassembly.DisassemblyTransformer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * This type represents the converter of arrays to lists
 * Created by kfgodel on 20/11/14.
 */
public class MapDisassembler implements Function<Object, Object> {

    private DisassemblyTransformer valueTransformer;

    @Override
    public Object apply(Object value) {
        Map<String, Object> asMap = (Map) value;

        Map<String, Object> translatedMap = new LinkedHashMap<>();
        Set<Map.Entry<String, Object>> sourceEntries = asMap.entrySet();
        for (Map.Entry<String, Object> sourceEntry : sourceEntries) {
            translatedMap.put(sourceEntry.getKey(), valueTransformer.transform(sourceEntry.getValue()));
        }
        return translatedMap;
    }

    public static MapDisassembler create(DisassemblyTransformer converter) {
        MapDisassembler arrayToListConverter = new MapDisassembler();
        arrayToListConverter.valueTransformer = converter;
        return arrayToListConverter;
    }

}
