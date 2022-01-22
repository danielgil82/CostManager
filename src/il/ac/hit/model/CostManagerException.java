package il.ac.hit.model;

import il.ac.hit.auxiliary.HandlingMessage;

/**
 * This class represents our specific exception that can be thrown in our application.
 * A "CostManagerException" is a type of Exception.
 */
public class CostManagerException extends Exception {
    /**
     * Ctor that receives the enum type of message.
     * @param exceptionsHandlingStrings - specific exception from type HandlingMessage.
     */
    public CostManagerException(HandlingMessage exceptionsHandlingStrings) {
        super(exceptionsHandlingStrings.toString());
    }

    /**
     * Ctor that receives the enum type of message and the cause of another exception.
     * @param exceptionsHandlingStrings - specific exception from type HandlingMessage.
     * @param cause - the cause of the exception.
     */
    public CostManagerException(HandlingMessage exceptionsHandlingStrings, Throwable cause) {
        super(exceptionsHandlingStrings.toString(), cause);
    }
}