package ar.com.dgarcia.objectmapper.api.ensemble.disassembly;

import ar.com.kfgodel.diamond.api.types.TypeInstance;

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
    Function<Object, Object> getTransformerFor(TypeInstance valueType);

    /**
     * Adds a custom transformer for a specific type
     * @param customType The type that needs a custom transformer
     * @param customTransformer The function to use for that type when disassembling
     */
    <T> void addTransformerFor(TypeInstance customType, Function<? super T, ?> customTransformer);

}
