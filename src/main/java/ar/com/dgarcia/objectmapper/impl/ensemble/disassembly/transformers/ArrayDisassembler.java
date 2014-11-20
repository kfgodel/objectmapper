package ar.com.dgarcia.objectmapper.impl.ensemble.disassembly.transformers;

import ar.com.dgarcia.objectmapper.api.ensemble.disassembly.DisassemblyTransformer;

import java.util.ArrayList;
import java.util.function.Function;

/**
 * This type represents the converter of arrays to lists
 * Created by kfgodel on 20/11/14.
 */
public class ArrayDisassembler implements Function<Object, Object> {

    private DisassemblyTransformer valueTransformer;

    @Override
    public Object apply(Object value) {
        Object[] asArray = (Object[]) value;
        ArrayList<Object> disassembledContent = new ArrayList<>(asArray.length);
        for (int i = 0; i < asArray.length; i++) {
            Object original = asArray[i];
            Object disassembled = valueTransformer.transform(original);
            disassembledContent.add(disassembled);
        }
        return disassembledContent;
    }

    public static ArrayDisassembler create(DisassemblyTransformer genericTransformer) {
        ArrayDisassembler arrayDisassembler = new ArrayDisassembler();
        arrayDisassembler.valueTransformer = genericTransformer;
        return arrayDisassembler;
    }

}
