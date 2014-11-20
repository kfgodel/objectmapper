package ar.com.dgarcia.objectmapper.api.ensemble.disassembly;

import java.util.function.Function;

/**
 * This type represents a value transformer to disassemble object properties
 * Created by kfgodel on 20/11/14.
 */
public interface DisassemblyTransformer {

    /**
     * Transforms the given value to a disassembled version of it
     * @param element The object to convert
     * @return The disassembled version
     */
    Object transform(Object element);

    /**
     * Returns the converter to use for the given value type
     */
    Function<Object, Object> getTransformerFor(Class<?> valueType);
}
