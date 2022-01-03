package il.ac.hit.auxiliary;

/**
 * The purpose of this class is to create a message and display it to the user.
 */
public class Message {

    private String content;

    public Message(String content) {
        this.content = content;
    }

    public String getMessage() {
        return content;
    }
}
