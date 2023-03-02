package com.app.backend.auxiliaryClasses;

/**
 * An instance of exceptional behavior in this library.
 */
@SuppressWarnings("serial")
public class LoadTesterException extends RuntimeException {

    public LoadTesterException(String reason) {
        super(reason);
    }

    public LoadTesterException(Exception e) {
        super(e);
    }
}
