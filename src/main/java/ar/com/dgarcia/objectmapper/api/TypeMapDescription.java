package ar.com.dgarcia.objectmapper.api;

/**
 * This type represents the Map description of a type
 * Created by kfgodel on 19/11/14.
 */
public interface TypeMapDescription {
    /**
     * The properties to create in a map representation
     * @return The type properties
     */
    PropertyEntryDescription[] getProperties();
}
