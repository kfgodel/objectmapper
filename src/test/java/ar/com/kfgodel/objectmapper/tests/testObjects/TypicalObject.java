package ar.com.kfgodel.objectmapper.tests.testObjects;

import java.util.*;
import java.util.stream.Stream;

/**
 * This type serves as a test object for conversion tests
 * Created by kfgodel on 18/11/14.
 */
public class TypicalObject {

    private int intPrimitive;
    public static final String intPrimitive_FIELD = "intPrimitive";
    
    private String stringPrimitive;
    public static final String stringPrimitive_FIELD = "stringPrimitive";

    private TestEnum enumPrimitive;
    public static final String enumPrimitive_FIELD = "enumPrimitive";

    private String[] arrayPrimitive;
    public static final String arrayPrimitive_FIELD = "arrayPrimitive";
    
    private TypicalObject otherObject;
    public static final String otherObject_FIELD = "otherObject";
    
    private List<TypicalObject> otherObjects;
    public static final String otherObjects_FIELD = "otherObjects";
    
    private ReferencedObject anotherReference;
    public static final String anotherReference_FIELD = "anotherReference";
    
    private Map<String, ReferencedObject> referencedMap;
    public static final String referencedMap_FIELD = "referencedMap";


    public int getIntPrimitive() {
        return intPrimitive;
    }

    public void setIntPrimitive(int intPrimitive) {
        this.intPrimitive = intPrimitive;
    }

    public String getStringPrimitive() {
        return stringPrimitive;
    }

    public void setStringPrimitive(String stringPrimitive) {
        this.stringPrimitive = stringPrimitive;
    }

    public String[] getArrayPrimitive() {
        return arrayPrimitive;
    }

    public void setArrayPrimitive(String[] arrayPrimitive) {
        this.arrayPrimitive = arrayPrimitive;
    }

    public TypicalObject getOtherObject() {
        return otherObject;
    }

    public void setOtherObject(TypicalObject otherObject) {
        this.otherObject = otherObject;
    }

    public List<TypicalObject> getOtherObjects() {
        return otherObjects;
    }

    public void setOtherObjects(List<TypicalObject> otherObjects) {
        this.otherObjects = otherObjects;
    }

    public ReferencedObject getAnotherReference() {
        return anotherReference;
    }

    public void setAnotherReference(ReferencedObject anotherReference) {
        this.anotherReference = anotherReference;
    }

    public Map<String, ReferencedObject> getReferencedMap() {
        return referencedMap;
    }

    public void setReferencedMap(Map<String, ReferencedObject> referencedMap) {
        this.referencedMap = referencedMap;
    }

    public TestEnum getEnumPrimitive() {
        return enumPrimitive;
    }

    public void setEnumPrimitive(TestEnum enumPrimitive) {
        this.enumPrimitive = enumPrimitive;
    }

    public static TypicalObject create() {
        TypicalObject typicalObject = new TypicalObject();
        return typicalObject;
    }

    public static TypicalObject create(int number) {
        TypicalObject object = create();
        object.initializeLeafTestData(number);
        return object;
    }


    public void initializeRootTestData(){
        this.intPrimitive = 0;
        this.enumPrimitive = TestEnum.VALUE_B;
        this.stringPrimitive = "aPrimitive";
        this.arrayPrimitive = new String[]{"uno", "dos", "tres"};
        this.otherObject = create(1);
        this.otherObjects = Arrays.asList(create(2), create(3));
        this.anotherReference = ReferencedObject.create(23, "John");
        this.referencedMap = new LinkedHashMap<>();
        this.referencedMap.put("1", ReferencedObject.create(1,"Primero"));
        this.referencedMap.put("2", ReferencedObject.create(2,"Segundo"));
    }

    public void initializeLeafTestData(int number){
        this.intPrimitive = number;
        this.stringPrimitive = "aPrimitive" + number;
        this.enumPrimitive = TestEnum.VALUE_A;
        this.arrayPrimitive = new String[]{};
        this.otherObject = null;
        this.otherObjects = new ArrayList<>();
        this.anotherReference = null;
        this.referencedMap = new LinkedHashMap<>();
    }

    public static Map<String, Object> createRootTestMap() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put(intPrimitive_FIELD, 0);
        map.put(stringPrimitive_FIELD, "aPrimitive");
        map.put(enumPrimitive_FIELD, TestEnum.VALUE_B.name());
        map.put(arrayPrimitive_FIELD, Arrays.asList("uno", "dos", "tres"));
        map.put(otherObject_FIELD, createLeafTestMap(1));
        map.put(otherObjects_FIELD, Arrays.asList(createLeafTestMap(2), createLeafTestMap(3)));
        map.put(anotherReference_FIELD, ReferencedObject.createMap(23, "John"));

        Map<String, Object> otherReferencedMap = new LinkedHashMap<>();
        otherReferencedMap.put("1", ReferencedObject.createMap(1, "Primero"));
        otherReferencedMap.put("2", ReferencedObject.createMap(2, "Segundo"));
        map.put(referencedMap_FIELD, otherReferencedMap);
        return map;
    }

    public static Map<String, Object> createLeafTestMap(int number){
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put(intPrimitive_FIELD, number);
        map.put(stringPrimitive_FIELD, "aPrimitive" + number);
        map.put(enumPrimitive_FIELD, TestEnum.VALUE_A.name());
        map.put(arrayPrimitive_FIELD, Arrays.asList());
        map.put(otherObject_FIELD, null);
        map.put(otherObjects_FIELD, Arrays.asList());
        map.put(anotherReference_FIELD, null);
        map.put(referencedMap_FIELD, new LinkedHashMap<>());
        return map;
    }

    @Override
    public boolean equals(Object obj) {
        TypicalObject other = (TypicalObject) obj;
        if(!this.referencedMap.equals(other.referencedMap)){
            return false;
        }

        return Stream.of(obj)
                .filter(TypicalObject.class::isInstance)
                .map(TypicalObject.class::cast)
                .filter((that) -> Objects.equals(this.stringPrimitive, that.stringPrimitive))
                .filter((that) -> Arrays.equals(this.arrayPrimitive, that.arrayPrimitive))
                .filter((that) -> Objects.equals(this.otherObject, that.otherObject))
                .filter((that) -> Objects.equals(this.otherObjects, that.otherObjects))
                .filter((that) -> Objects.equals(this.referencedMap, that.referencedMap))
                .anyMatch((that) -> true);
    }
}
