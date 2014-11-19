package ar.com.kfgodel.objectmapper.tests.testObjects;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This type serves as a test object for conversion tests
 * Created by kfgodel on 18/11/14.
 */
public class ReferencedObject {

    private Integer age;
    public static final String age_FIELD = "age";

    private String name;
    public static final String name_FIELD = "name";


    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static ReferencedObject create(Integer age, String name) {
        ReferencedObject object = new ReferencedObject();
        object.age = age;
        object.name = name;
        return object;
    }

    public static Map<String, Object> createMap(Integer age, String name){
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put(age_FIELD, age);
        map.put(name_FIELD, name);
        return map;
    }

}
