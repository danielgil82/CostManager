package il.ac.hit.model;

import il.ac.hit.auxiliary.HandlingMessage;

public class CostManagerException extends Exception
{
//    public CostManagerException(String message)
//    {
//        super(message);
//    }
//
//    public CostManagerException(String message, Throwable cause) {
//        super(message, cause);
//    }

    public CostManagerException(HandlingMessage exceptionsHandlingStrings) {
        super(exceptionsHandlingStrings.toString());
    }

    public CostManagerException(HandlingMessage exceptionsHandlingStrings, Throwable cause) {
        super(exceptionsHandlingStrings.toString(), cause);
    }



}
