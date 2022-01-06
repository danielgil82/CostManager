package il.ac.hit.model;

/**
 * The User class represents a mapping class that represents the users' table in the database.
 */
public class User {
    /** Data members that represent the users' table attributes. */
    private int userID;
    private String fullName;
    private String usersPassword;

    /**
     * Ctor that receives the user attributes including the userID.
     * @param userID - the id of the user.
     * @param fullName - the name of the user.
     * @param usersPassword - the password of the user.
     */
    public User(int userID, String fullName, String usersPassword) {
        this(fullName, usersPassword);
        setID(userID);
    }

    /** Ctor that receives the user attributes excluding the userID.
     * @param fullName - the name of the user.
     * @param usersPassword - the password of the user.
     */
    public User(String fullName, String usersPassword) {
        setFullName(fullName);
        setUsersPassword(usersPassword);
    }

    /** Getters */
    public int getUserID() {
        return userID;
    }

    public String getUsersPassword() {
        return usersPassword;
    }

    public String getFullName() {
        return fullName;
    }

    /** Setters */
    public void setID(int id) {
        this.userID = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setUsersPassword(String usersPassword) {
        this.usersPassword = usersPassword;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + userID +
                ", fullName='" + fullName + '\'' +
                ", usersPassword='" + usersPassword + '\'' +
                '}';
    }
}