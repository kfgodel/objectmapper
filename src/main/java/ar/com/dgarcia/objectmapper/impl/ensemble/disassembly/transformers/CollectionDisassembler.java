package ar.com.dgarcia.objectmapper.impl.ensemble.disassembly.transformers;

import ar.com.dgarcia.objectmapper.api.ensemble.disassembly.DisassemblyTransformer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

/**
 * This type represents the converter of arrays to lists
 * Created by kfgodel on 20/11/14.
 */
public class CollectionDisassembler implements Function<Object, Object> {

    private DisassemblyTransformer valueTransformer;

    @Override
    public Object apply(Object value) {
        Collection<Object> asCollection = (Collection) value;
        ArrayList<Object> objects = new ArrayList<>(asCollection.size());
        for (Object element : asCollection) {
            objects.add(valueTransformer.transform(element));
        }
        return objects;
    }

    public static CollectionDisassembler create(DisassemblyTransformer converter) {
        CollectionDisassembler arrayToListConverter = new CollectionDisassembler();
        arrayToListConverter.valueTransformer = converter;
        return arrayToListConverter;
    }

}
