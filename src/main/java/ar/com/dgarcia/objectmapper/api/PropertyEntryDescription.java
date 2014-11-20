package ar.com.dgarcia.objectmapper.api;

/**
 * This type represents the entry description for a type property
 * Created by kfgodel on 19/11/14.
 */
public interface PropertyEntryDescription {
    /**
     * @return The name to identify this property
     */
    String getName();

    /**
     * Applies this property to the instance in order to get the value in that object
     * @param instance The instance to retrieve the value from
     * @return The value of the property
     */
    Object getValueFrom(Object instance);
}
