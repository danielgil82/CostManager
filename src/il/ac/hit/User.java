package il.ac.hit;

public class User
{
    private int userID;
    private String fullName;
    private String usersPassword;

    public User(int userID, String fullName, String usersPassword)
    {
      setID(userID);
      setFullName(fullName);
      setUsersPassword(usersPassword);
    }

    public User(String fullName, String usersPassword)
    {
        setFullName(fullName);
        setUsersPassword(usersPassword);
    }

    public int getUserID()
    {
        return userID;
    }

    public String getUsersPassword()
    {
        return usersPassword;
    }

    public String getFullName()
    {
        return fullName;
    }

    public void setID(int id)
    {
        this.userID = id;
    }

    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }

    public void setUsersPassword(String usersPassword)
    {
        this.usersPassword = usersPassword;
    }

    @Override
    public String toString()
    {
        return "User{" +
                "id=" + userID +
                ", fullName='" + fullName + '\'' +
                ", usersPassword='" + usersPassword + '\'' +
                '}';
    }
}