package ar.com.dgarcia.objectmapper.impl.ensemble.disassembly.transformers;

import java.util.function.Function;

/**
 * This type represents an enum dissasembler that uses only the name of the enum as representation
 * Created by ikari on 27/01/2015.
 */
public class EnumDisassembler implements Function<Object, Object> {

    @Override
    public Object apply(Object o) {
        Enum<?> enumValue = (Enum<?>) o;
        String enumName = enumValue.name();
        return enumName;
    }

    public static EnumDisassembler create() {
        EnumDisassembler disassembler = new EnumDisassembler();
        return disassembler;
    }

}
