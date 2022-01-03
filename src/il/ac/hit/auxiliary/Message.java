package il.ac.hit.auxiliary;

/**
 * The purpose of this class is to create a message and display it to the user.
 */
public class Message {
    // String data member that represents the message
    private String content;

    /**
     * ctor that gets the string that represents the content of the message to display.
     * @param content
     */
    public Message(String content) {
        this.content = content;
    }

    // getter
    public String getMessage() {
        return content;
    }
}
