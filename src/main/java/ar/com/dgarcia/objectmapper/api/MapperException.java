package ar.com.dgarcia.objectmapper.api;

/**
 * This type represents an error on mapping types
 * Created by kfgodel on 27/01/15.
 */
public class MapperException extends RuntimeException {

    public MapperException(String message) {
        super(message);
    }

    public MapperException(String message, Throwable cause) {
        super(message, cause);
    }

    public MapperException(Throwable cause) {
        super(cause);
    }
}
