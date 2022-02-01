package il.ac.hit.auxiliary;

/**
 * The purpose of this class is to create a message and display it to the user.
 */
public class Message {

    /**
     * String data member that represents the message.
     */
    private String content;

    /**
     * Ctor that gets the string that represents the content of the message to display.
     *
     * @param content of the message
     */
    public Message(String content) {
        this.content = content;
    }

    /**
     * Getter
     *
     * @return the content of the message.
     */
    public String getMessage() {
        return content;
    }

    /**
     * Override to toString()
     *
     * @return the content of the class via a string.
     */
    @Override
    public String toString() {
        return super.toString();
    }
}