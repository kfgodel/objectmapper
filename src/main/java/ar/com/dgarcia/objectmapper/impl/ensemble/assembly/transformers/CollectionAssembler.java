package ar.com.dgarcia.objectmapper.impl.ensemble.assembly.transformers;

import ar.com.dgarcia.objectmapper.api.ensemble.assembly.AssemblyTransformer;
import ar.com.kfgodel.diamond.api.types.TypeInstance;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * This type represents the collection assembler for expected collections
 * Created by kfgodel on 21/11/14.
 */
public class CollectionAssembler implements Function<Object, Object> {

    private AssemblyTransformer valueTransformer;
    private Supplier<Collection<Object>> collectionSupplier;
    private TypeInstance elementType;

    @Override
    public Object apply(Object value) {
        Collection<Object> asCollection = (Collection) value;
        Collection<Object> objects = collectionSupplier.get();
        for (Object element : asCollection) {
            objects.add(valueTransformer.transform(element, elementType));
        }
        return objects;
    }

    public static CollectionAssembler create(AssemblyTransformer converter, TypeInstance collectionType) {
        CollectionAssembler assembler = new CollectionAssembler();
        assembler.valueTransformer = converter;
        assembler.collectionSupplier = defineSupplierFor(collectionType);
        assembler.elementType = collectionType.generics().arguments().findFirst().get();
        return assembler;
    }

    private static Supplier<Collection<Object>> defineSupplierFor(TypeInstance collectionType) {
        Class<?> nativeType = collectionType.nativeTypes().get();
        if(!nativeType.isInterface()){
            // It's a concrete type, use default supplier
            return (Supplier)collectionType.constructors().niladic().get();
        }
        // We are in troubles, if we get lucky they are using just the generic interface

        if(Set.class.equals(nativeType)){
            //It's type of set
            return LinkedHashSet::new;
        }
        if(List.class.equals(nativeType) || Collection.class.equals(nativeType)) {
            return ArrayList::new;
        }
        throw new RuntimeException("We don't know how to create instances of " + nativeType);
    }
}
