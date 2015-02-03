package ar.com.dgarcia.objectmapper.api.ensemble.assembly;

import ar.com.kfgodel.diamond.api.types.TypeInstance;

import java.util.function.Function;

/**
 * This type represents the generic converter of values when assembling an instance
 * Created by kfgodel on 21/11/14.
 */
public interface AssemblyTransformer {
    /**
     * Gets the transformer needed according to the expected type
     * @param valueType The transformer to use according to the destination type
     * @return The transformer to use
     */
    Function<Object,Object> getTransformerFor(TypeInstance valueType);

    /**
     * Transforms the given value in the expected type
     * @param value The value to transform
     * @param expectedType The type of expected instance
     * @return The converted instance
     */
    public Object transform(Object value, TypeInstance expectedType);
    
    /**
     * Adds a custom transformer for a specific type
     * @param typicalObjectClass The type that needs a custom transformer
     * @param customTransformer The function to use for that type when assembling
     */
    <T> void addTransformerFor(TypeInstance typicalObjectClass, Function<?, ? extends T> customTransformer);
    
}
